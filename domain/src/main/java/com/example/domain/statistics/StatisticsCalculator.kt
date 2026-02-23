package com.example.domain.statistics

import com.example.domain.model.Movie
import kotlin.math.ln
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Year

private const val TOP_GENRES_LIMIT = 10
private const val TEMPORAL_MIN_MOVIES = 3
private const val DIVERSITY_MIN_MOVIES = 3
private const val RATING_MIN_MOVIES = 3
private const val MODERNITY_BASE_YEAR = 1900

private fun List<Movie>.ratingsOrNull(): List<Double>? {
    val list = mapNotNull { m -> m.rating?.kp ?: m.rating?.imdb }
    return list.takeIf { it.size >= RATING_MIN_MOVIES }
}

/**
 * Calculates statistics from user's watched list.
 * All calculations are performed locally; no external analytics.
 */
object StatisticsCalculator {

    /**
     * Weighted Genre Score (User Preference Index).
     * GenreScore(g) = Σ (Rating_i × W_i(g)) / N
     * where W_i(g) = 1/k_i if movie i has genre g, else 0 (k_i = number of genres of movie i).
     *
     * @param watched List of movies from the Watched list.
     * @return Top N genres by score descending (N = min(10, number of genres)), or null if fewer than 3 valid movies.
     */
    fun calculateWeightedGenreScores(watched: List<Movie>): List<GenreScore>? {
        val valid = watched.filter { movie ->
            val hasRating = movie.rating?.kp != null || movie.rating?.imdb != null
            val hasGenres = movie.genres.isNotEmpty()
            hasRating && hasGenres
        }
        if (valid.size < 3) return null

        val n = valid.size
        val genreSums = mutableMapOf<String, Double>()

        for (movie in valid) {
            val rating = movie.rating?.kp ?: movie.rating?.imdb ?: continue
            val k = movie.genres.size
            if (k <= 0) continue
            val weight = 1.0 / k
            val contribution = rating * weight
            for (genre in movie.genres) {
                val name = genre.name?.takeIf { it.isNotBlank() } ?: continue
                genreSums[name] = (genreSums[name] ?: 0.0) + contribution
            }
        }

        return genreSums.map { (name, sum) ->
            val score = BigDecimal(sum / n).setScale(2, RoundingMode.HALF_UP).toDouble()
            GenreScore(genreName = name, score = score)
        }
            .sortedByDescending { it.score }
            .take(TOP_GENRES_LIMIT)
    }

    /**
     * Temporal balance: average year and modernity index.
     * AverageYear = Σ Year_i / N
     * ModernityIndex = (AverageYear - 1900) / (CurrentYear - 1900), clamped to 0..1
     *
     * @param watched List of movies from the Watched list.
     * @return TemporalBalance or null if fewer than 3 movies with non-null year.
     */
    fun calculateTemporalBalance(watched: List<Movie>): TemporalBalance? {
        val years = watched.mapNotNull { it.year }
        if (years.size < TEMPORAL_MIN_MOVIES) return null
        val n = years.size
        val averageYear = (years.sum().toDouble() / n).toInt()
        val currentYear = Year.now().value
        val range = (currentYear - MODERNITY_BASE_YEAR).toDouble()
        val modernityIndex = ((averageYear - MODERNITY_BASE_YEAR) / range).coerceIn(0.0, 1.0)
        val varianceYears = years.map { (it - averageYear).toDouble() * (it - averageYear) }.sum() / n
        return TemporalBalance(
            averageYear = averageYear,
            modernityIndex = BigDecimal(modernityIndex).setScale(2, RoundingMode.HALF_UP).toDouble(),
            varianceYears = BigDecimal(varianceYears).setScale(2, RoundingMode.HALF_UP).toDouble(),
        )
    }

    /**
     * Diversity Index (Shannon entropy): H = - Σ (p_i × ln(p_i))
     * where p_i = proportion of genre occurrences for genre i (Σ p_i = 1).
     * Low H → few dominant genres; high H → more even spread.
     *
     * @param watched List of movies from the Watched list.
     * @return Diversity index value (2 decimals), or null if fewer than 3 movies with genres.
     */
    fun calculateDiversityIndex(watched: List<Movie>): Double? {
        val valid = watched.filter { it.genres.isNotEmpty() }
        if (valid.size < DIVERSITY_MIN_MOVIES) return null
        val genreCounts = mutableMapOf<String, Int>()
        for (movie in valid) {
            for (genre in movie.genres) {
                val name = genre.name?.takeIf { it.isNotBlank() } ?: continue
                genreCounts[name] = (genreCounts[name] ?: 0) + 1
            }
        }
        val total = genreCounts.values.sum()
        if (total == 0) return null
        var h = 0.0
        for (count in genreCounts.values) {
            val p = count.toDouble() / total
            if (p > 0.0) h -= p * ln(p)
        }
        return BigDecimal(h).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    /**
     * Average rating of watched movies (KP or IMDB).
     * MeanRating = Σ Rating_i / N
     *
     * @param watched List of movies from the Watched list.
     * @return Mean rating (2 decimals), or null if fewer than 3 movies with rating.
     */
    fun calculateAverageRating(watched: List<Movie>): Double? {
        val ratings = watched.ratingsOrNull() ?: return null
        val mean = ratings.sum() / ratings.size
        return BigDecimal(mean).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    /**
     * Variance of ratings: σ² = Σ (r_i - mean)² / N.
     * Caller can show std dev as sqrt(variance).
     *
     * @param watched List of movies from the Watched list.
     * @return Variance (2 decimals), or null if fewer than 3 movies with rating.
     */
    fun calculateRatingVariance(watched: List<Movie>): Double? {
        val ratings = watched.ratingsOrNull() ?: return null
        val n = ratings.size
        val mean = ratings.sum() / n
        val variance = ratings.map { (it - mean) * (it - mean) }.sum() / n
        return BigDecimal(variance).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    /**
     * Bias of user ratings vs KP: Bias = Σ (UserRate_i - Kp_i) / N.
     * Positive = user rates higher than KP on average.
     *
     * @param watched List of movies from the Watched list (with userRate loaded).
     * @return Bias (2 decimals), or null if fewer than 3 movies with both userRate and KP/IMDB rating.
     */
    fun calculateRatingBias(watched: List<Movie>): Double? {
        val pairs = watched.mapNotNull { m ->
            val ur = m.userRate ?: return@mapNotNull null
            val kp = m.rating?.kp ?: m.rating?.imdb ?: return@mapNotNull null
            ur.toDouble() to kp
        }
        if (pairs.size < RATING_MIN_MOVIES) return null
        val bias = pairs.map { (ur, kp) -> ur - kp }.sum() / pairs.size
        return BigDecimal(bias).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    /**
     * Completion rate: C = W / (W + D).
     * W = watched (finished), D = dropped. Returns null when W + D == 0.
     *
     * @param watchedCount Number of movies watched to the end.
     * @param droppedCount Number of movies dropped.
     * @return CompletionRate or null when no started movies.
     */
    fun calculateCompletionRate(watchedCount: Int, droppedCount: Int): CompletionRate? {
        val total = watchedCount + droppedCount
        if (total == 0) return null
        val ratio = watchedCount.toDouble() / total
        val percent = (ratio * 100).toInt().coerceIn(0, 100)
        val ratioRounded = BigDecimal(ratio).setScale(2, RoundingMode.HALF_UP).toDouble()
        return CompletionRate(
            watchedCount = watchedCount,
            droppedCount = droppedCount,
            totalStarted = total,
            completionRatio = ratioRounded,
            completionPercent = percent,
        )
    }
}
