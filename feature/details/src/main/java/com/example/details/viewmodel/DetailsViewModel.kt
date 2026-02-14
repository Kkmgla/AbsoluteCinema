package com.example.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Movie
import com.example.domain.model.MovieAward
import com.example.domain.model.MovieImage
import com.example.domain.model.Review
import com.example.domain.model.Studio
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _movie = MutableStateFlow<Movie>(Movie())
    val movie = _movie.asStateFlow()

    private val _awards = MutableStateFlow<List<MovieAward>>(emptyList())
    val awards = _awards.asStateFlow()

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews = _reviews.asStateFlow()

    private val _images = MutableStateFlow<List<MovieImage>>(emptyList())
    val images = _images.asStateFlow()

    private val _studios = MutableStateFlow<List<Studio>>(emptyList())
    val studios = _studios.asStateFlow()

    fun updateMovie(movieId: Int) = viewModelScope.launch {
        _movie.value = repository.getMovieById(id = movieId)

        _reviews.value = try {
            repository.getMovieReviews(movieId = movieId)
        } catch (_: Exception) {
            emptyList()
        }

        _images.value = try {
            repository.getMovieImages(movieId = movieId)
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun setUserScore(score: Int) = viewModelScope.launch{
        _movie.value.id?.let { id ->
            repository.setUserRateToMovie(id, rate = score)
            updateMovie(id)
        }
    }

    fun deleteUserScore() = viewModelScope.launch{
        _movie.value.id?.let { id ->
            repository.removeUserRateFromMovie(id)
            updateMovie(id)
        }
    }

    fun addToWillWatch() = viewModelScope.launch{
        _movie.value.id?.let { id ->
            repository.addMovieToWillWatch(id)
            updateMovie(id)
        }
    }

    fun removeFromWillWatch() = viewModelScope.launch{
        _movie.value.id?.let { id ->
            repository.removeMovieFromWillWatch(id)
            updateMovie(id)
        }
    }

    fun addToFavourite() = viewModelScope.launch{
        _movie.value.id?.let { id ->
            repository.addMovieToFavourites(id)
            updateMovie(id)
        }
    }

    fun removeFromFavourite() = viewModelScope.launch{
        _movie.value.id?.let { id ->
            repository.removeMovieFromFavourites(id)
            updateMovie(id)
        }
    }

}