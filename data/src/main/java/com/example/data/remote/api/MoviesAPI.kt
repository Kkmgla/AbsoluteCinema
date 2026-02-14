package com.example.data.remote.api

import com.example.data.remote.dto.common.FilterDto
import com.example.data.remote.dto.common.MovieDto
import com.example.data.remote.dto.responce.ImageResponseDto
import com.example.data.remote.dto.responce.MovieAwardsResponseDto
import com.example.data.remote.dto.responce.ReviewResponseDto
import com.example.data.remote.dto.responce.MoviesResponseDto
import com.example.data.remote.dto.responce.StudioResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {


    /**
     * Выполняет поиск по названию.
     * Ключ X-API-KEY добавляется в OkHttp Interceptor (см. DataModule).
     *
     * [query] - название тайтла
     */
    @GET("v1.4/movie/search")
    suspend fun searchMovieByName(@Query("query") query: String): MoviesResponseDto

    @GET("v1.4/movie/{id}")
    suspend fun getMovieById(@Path("id") id: Int): MovieDto

    /**
     * Выполняет поиск с различными фильтрами.
     *
     *
     * - [fields] - сортировка по полям из MoviesDTO (year, rating.kp...)
     * - [sortTypes] - тип сортировки для каждого из полей [fields] ("1", "-1")
     * - [types] - поиск по типам:
     *      - movie
     *      - tv-series
     *      - cartoon
     *      - animated-series
     *      - anime
     *
     *      Пример: {"movie", "tv-series", "!anime"}
     *
     * - [isSeries] - является ли сериалом
     * - [years] - года, как по отдельности, так и диапазоном
     *
     *      Пример: {1874, 2050, !2020, 2020-2024}
     *
     * - [kpRating] - поиск по рейтингу Кинопоиска
     *
     *      Пример: { 7, 10, 7.2-10 }
     *
     * - [genres] - жанры
     *
     *      Пример: { "драма", "комедия", "!мелодрама", "+ужасы" }
     *
     * - [countries] - страны
     *
     *      Пример: { "США", "Россия", "!Франция" , "+Великобритания" }
     *
     * - [inCollection] - вхождение в какую-либо коллекцию
     *
     *      Пример: { "top250", "top-100-indian-movies", "!top-100-movies" }
     */
    @GET("v1.4/movie")
    suspend fun searchWithFilter(
        @Query("sortField") fields: List<String>? = null,
        @Query("sortType") sortTypes: List<Int>? = null,
        @Query("type") types: List<String>? = null,
        @Query("isSeries") isSeries: Boolean? = null,
        @Query("year") years: List<String>? = null,
        @Query("rating.kp") kpRating: List<String>? = null,
        @Query("genres.name") genres: List<String>? = null,
        @Query("countries.name") countries: List<String>? = null,
        @Query("lists") inCollection: List<String>? = null,
    ): MoviesResponseDto

    /**
     * Запрос получения доступных фильтров для поиска.
     * Например: genres.name, countries.name.
     *
     * @param field искомое поле.
     * @return список из [FilterDto], содержащий найденные фильтры.
     */
    @GET("v1/movie/possible-values-by-field")
    suspend fun getFiltersByFields(@Query("field") field: String): List<FilterDto>

    @GET("v1.4/movie/awards")
    suspend fun getMovieAwards(
        @Query("movieId") movieId: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ): MovieAwardsResponseDto

    @GET("v1.4/review")
    suspend fun getMovieReviews(
        @Query("movieId") movieId: List<Int>,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
    ): ReviewResponseDto

    @GET("v1.4/image")
    suspend fun getMovieImages(
        @Query("movieId") movieId: List<Int>,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
    ): ImageResponseDto

    @GET("v1.4/studio")
    suspend fun getMovieStudios(
        @Query("movieId") movieId: Int,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10,
    ): StudioResponseDto
}