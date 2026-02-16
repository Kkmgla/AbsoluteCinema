package com.example.users.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.example.core.ui.CommonMovieListScreen
import com.example.domain.model.Movie
import com.example.users.viewmodel.UsersViewModel

@Composable
fun AllWillWatchScreen(
    paddingValues: PaddingValues,
    viewModel: UsersViewModel,
    onMovieClicked: (Movie) -> Unit,
    onBackClicked: () -> Unit,
) {
    val title = stringResource(com.example.core.R.string.WillWatch)
    val list = viewModel.willWatch.collectAsState(emptyList())

    CommonMovieListScreen(
        paddingValues = paddingValues,
        title = title,
        moviesList = list.value,
        onBackClicked = onBackClicked,
        onMovieClicked = onMovieClicked,
    )
}

@Composable
fun AllYourRatesScreen(
    paddingValues: PaddingValues,
    viewModel: UsersViewModel,
    onMovieClicked: (Movie) -> Unit,
    onBackClicked: () -> Unit,
) {
    val title = stringResource(com.example.core.R.string.YourRates)
    val list = viewModel.userRates.collectAsState(emptyList())

    CommonMovieListScreen(
        paddingValues = paddingValues,
        title = title,
        moviesList = list.value,
        onBackClicked = onBackClicked,
        onMovieClicked = onMovieClicked,
    )
}

@Composable
fun AllFavouritesScreen(
    paddingValues: PaddingValues,
    viewModel: UsersViewModel,
    onMovieClicked: (Movie) -> Unit,
    onBackClicked: () -> Unit,
) {
    val title = stringResource(com.example.core.R.string.Favourite)
    val list = viewModel.favourites.collectAsState(emptyList())

    CommonMovieListScreen(
        paddingValues = paddingValues,
        title = title,
        moviesList = list.value,
        onBackClicked = onBackClicked,
        onMovieClicked = onMovieClicked,
    )
}

@Composable
fun AllWatchingScreen(
    paddingValues: PaddingValues,
    viewModel: UsersViewModel,
    onMovieClicked: (Movie) -> Unit,
    onBackClicked: () -> Unit,
) {
    val title = stringResource(com.example.core.R.string.Watching)
    val list = viewModel.watching.collectAsState(emptyList())

    CommonMovieListScreen(
        paddingValues = paddingValues,
        title = title,
        moviesList = list.value,
        onBackClicked = onBackClicked,
        onMovieClicked = onMovieClicked,
    )
}

@Composable
fun AllWatchedScreen(
    paddingValues: PaddingValues,
    viewModel: UsersViewModel,
    onMovieClicked: (Movie) -> Unit,
    onBackClicked: () -> Unit,
) {
    val title = stringResource(com.example.core.R.string.Watched)
    val list = viewModel.watched.collectAsState(emptyList())

    CommonMovieListScreen(
        paddingValues = paddingValues,
        title = title,
        moviesList = list.value,
        onBackClicked = onBackClicked,
        onMovieClicked = onMovieClicked,
    )
}

@Composable
fun AllDroppedScreen(
    paddingValues: PaddingValues,
    viewModel: UsersViewModel,
    onMovieClicked: (Movie) -> Unit,
    onBackClicked: () -> Unit,
) {
    val title = stringResource(com.example.core.R.string.Dropped)
    val list = viewModel.dropped.collectAsState(emptyList())

    CommonMovieListScreen(
        paddingValues = paddingValues,
        title = title,
        moviesList = list.value,
        onBackClicked = onBackClicked,
        onMovieClicked = onMovieClicked,
    )
}
