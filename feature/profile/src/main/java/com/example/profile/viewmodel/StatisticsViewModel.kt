package com.example.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.MovieRepository
import com.example.domain.statistics.StatisticsCalculator
import com.example.domain.statistics.TemporalBalance
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StatisticsViewModel(
    repository: MovieRepository,
) : ViewModel() {

    val topGenreScores = repository.getWatchedMovies()
        .map { list -> StatisticsCalculator.calculateWeightedGenreScores(list) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val temporalBalance = repository.getWatchedMovies()
        .map { list -> StatisticsCalculator.calculateTemporalBalance(list) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val diversityIndex = repository.getWatchedMovies()
        .map { list -> StatisticsCalculator.calculateDiversityIndex(list) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val averageRating = repository.getWatchedMovies()
        .map { list -> StatisticsCalculator.calculateAverageRating(list) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val ratingVariance = repository.getWatchedMovies()
        .map { list -> StatisticsCalculator.calculateRatingVariance(list) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val ratingBias = repository.getWatchedMovies()
        .map { list -> StatisticsCalculator.calculateRatingBias(list) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val completionRate = combine(
        repository.getWatchedMovies().map { it.size },
        repository.getDroppedMovies().map { it.size },
    ) { watchedSize, droppedSize ->
        StatisticsCalculator.calculateCompletionRate(watchedSize, droppedSize)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null,
    )
}
