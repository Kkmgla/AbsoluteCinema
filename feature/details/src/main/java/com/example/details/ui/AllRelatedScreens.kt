package com.example.details.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.core.R
import com.example.core.ui.CommonMovieListScreen
import com.example.domain.model.Movie
import com.example.details.viewmodel.DetailsViewModel

@Composable
fun AllSequelsPrequelsScreen(
    paddingValues: PaddingValues,
    viewModel: DetailsViewModel,
    onMovieClicked: (Movie) -> Unit,
    onBackClicked: () -> Unit,
) {
    val title = stringResource(R.string.SequelsAndPrequels)
    val list = viewModel.allSequelsList.collectAsState(emptyList())

    CommonMovieListScreen(
        paddingValues = paddingValues,
        title = title,
        moviesList = list.value,
        onBackClicked = onBackClicked,
        onMovieClicked = onMovieClicked,
    )
}

@Composable
fun AllSimilarMoviesScreen(
    paddingValues: PaddingValues,
    viewModel: DetailsViewModel,
    onMovieClicked: (Movie) -> Unit,
    onBackClicked: () -> Unit,
) {
    val title = stringResource(R.string.SimilarMovies)
    val list = viewModel.allSimilarList.collectAsState(emptyList())

    CommonMovieListScreen(
        paddingValues = paddingValues,
        title = title,
        moviesList = list.value,
        onBackClicked = onBackClicked,
        onMovieClicked = onMovieClicked,
    )
}
