package com.example.absolutecinema.navigtion

import kotlinx.serialization.Serializable

sealed class ScreenRoutes {

    @Serializable
    object ScreenBegin

    @Serializable
    object ScreenHome

    @Serializable
    object ScreenSearch

    @Serializable
    object ScreenSearchFilters

    @Serializable
    object ScreenSearchFiltersResult

    @Serializable
    data class ScreenSearchTitleResult(val query: String = "")

    @Serializable
    object ScreenUsers

    @Serializable
    object ScreenProfile

    @Serializable
    object ScreenSettings

    @Serializable
    object ScreenStatistics

    @Serializable
    object ScreenBugReport

    @Serializable
    data class ScreenMovie(val movieId: Int)

    @Serializable
    object ScreenDescription

    @Serializable
    object ScreenRegistration

    @Serializable
    object ScreenLogin

    @Serializable
    object ScreenAllMovies

    @Serializable
    object ScreenAllSeries

    @Serializable
    object ScreenAllDetectives

    @Serializable
    object ScreenAllComedies

    @Serializable
    object ScreenAllRomans

    @Serializable
    object ScreenAllSequelsPrequels

    @Serializable
    object ScreenAllSimilarMovies

    @Serializable
    object ScreenAllWillWatch

    @Serializable
    object ScreenAllYourRates

    @Serializable
    object ScreenAllFavourites

    @Serializable
    object ScreenAllWatching

    @Serializable
    object ScreenAllWatched

    @Serializable
    object ScreenAllDropped
}