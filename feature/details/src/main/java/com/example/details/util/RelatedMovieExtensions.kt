package com.example.details.util

import com.example.domain.model.Movie
import com.example.domain.model.SeqAndPreq
import com.example.domain.model.SimilarMovie

/**
 * Converts [SeqAndPreq] to a minimal [Movie] for use in row cards and All screens.
 */
fun SeqAndPreq.toMovie(): Movie = Movie(
    id = id,
    name = name,
    enName = enName,
    alternativeName = alternativeName,
    type = type,
    poster = poster,
    rating = rating,
    year = year,
)

/**
 * Converts [SimilarMovie] to a minimal [Movie] for use in row cards and All screens.
 */
fun SimilarMovie.toMovie(): Movie = Movie(
    id = id ?: movieId,
    name = name,
    enName = enName,
    alternativeName = alternativeName,
    type = type,
    poster = poster,
    rating = rating,
    year = year,
)
