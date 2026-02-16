package com.example.users.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.ApiRatingBadge
import com.example.core.ui.LoadImageWithPlaceholder
import com.example.core.ui.UserRatingBadge
import com.example.core.util.getName
import com.example.domain.model.Movie
import com.example.users.viewmodel.UsersViewModel

private const val POSTER_WIDTH = 133
private const val POSTER_HEIGHT = 200

/**
 * Bookmark card: same structure as Home (Feed) — poster, title, year; rating overlay top-right on poster.
 * No genre. Matches Feed poster size (133x200), spacing, and title typography.
 */
@Composable
private fun BookmarkMovieCard(movie: Movie, onMovieClicked: () -> Unit) {
    val shape = RoundedCornerShape(12.dp)
    Column(
        modifier = Modifier
            .width(POSTER_WIDTH.dp)
            .padding(end = 8.dp)
            .clip(shape)
            .clickable { onMovieClicked.invoke() }
    ) {
        Box {
            LoadImageWithPlaceholder(
                imageUrl = movie.poster?.posterUrl,
                placeholderResId = com.example.core.R.drawable.poster_placeholder,
                modifier = Modifier
                    .height(POSTER_HEIGHT.dp)
                    .width(POSTER_WIDTH.dp)
                    .clip(shape),
                contentScale = ContentScale.Crop
            )
            ApiRatingBadge(
                movie = movie,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp, start = 8.dp)
            )
            UserRatingBadge(
                movie = movie,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
            )
        }
        Text(
            movie.getName(),
            color = MaterialTheme.colorScheme.primary,
            fontSize = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        if (movie.year != null) {
            Text(
                "(${movie.year})",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Горизонтальный список с фильмами пользователя.
 *
 * @param title заголовок категории.
 * @param list список фильмов.
 * @param onAllClicked функция вызывается при клике на кнопку "все" рядом с заголовком.
 * @param onMovieClicked функция вызывается при клике на фильм.
 */
@Composable
private fun HorizontalRowWTitleSmall(
    title: String,
    list: List<Movie>,
    onAllClicked: () -> Unit,
    onMovieClicked: (Movie) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
            .then(if (list.isNotEmpty()) Modifier.height(320.dp) else Modifier)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            if (list.isNotEmpty()) {
                Text(
                    stringResource(com.example.core.R.string.All),
                    fontSize = 20.sp,
                    color = colorResource(com.example.core.R.color.accent),
                    modifier = Modifier.clickable {
                        onAllClicked.invoke()
                    })
            }
        }
        if (list.isNotEmpty()) {
            LazyRow {
                items(list) { movie ->
                    BookmarkMovieCard(movie) {
                        onMovieClicked(movie)
                    }
                }
            }
        } else {
            Text(
                text = "В этом разделе еще нет фильмов",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}


/**
 * Экран фильмов пользователя.
 * Отображает такие разделы как:
 *  - Буду смотреть
 *  - Ваши оценки
 *  - Любимые
 *
 * @param onMovieClicked функция для навигации, вызывается при клике на любой из фильмов.
 */
@Composable
fun UsersScreen(
    paddingValues: PaddingValues = PaddingValues(),
    onMovieClicked: (Movie) -> Unit = {},
    onAllWillWatchClicked: () -> Unit = {},
    onAllYourRatesClicked: () -> Unit = {},
    onAllFavouritesClicked: () -> Unit = {},
    onAllWatchingClicked: () -> Unit = {},
    onAllWatchedClicked: () -> Unit = {},
    onAllDroppedClicked: () -> Unit = {},
    viewModel: UsersViewModel,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
    ) {

        HorizontalRowWTitleSmall(
            title = stringResource(com.example.core.R.string.WillWatch),
            list = viewModel.willWatch.collectAsState().value,
            onAllClicked = onAllWillWatchClicked,
            onMovieClicked = { movie -> onMovieClicked(movie) }
        )
        HorizontalRowWTitleSmall(
            title = stringResource(com.example.core.R.string.YourRates),
            list = viewModel.userRates.collectAsState().value,
            onAllClicked = onAllYourRatesClicked,
            onMovieClicked = { movie -> onMovieClicked(movie) }
        )
        HorizontalRowWTitleSmall(
            title = stringResource(com.example.core.R.string.Favourite),
            list = viewModel.favourites.collectAsState().value,
            onAllClicked = onAllFavouritesClicked,
            onMovieClicked = { movie -> onMovieClicked(movie) }
        )
        HorizontalRowWTitleSmall(
            title = stringResource(com.example.core.R.string.Watching),
            list = viewModel.watching.collectAsState().value,
            onAllClicked = onAllWatchingClicked,
            onMovieClicked = { movie -> onMovieClicked(movie) }
        )
        HorizontalRowWTitleSmall(
            title = stringResource(com.example.core.R.string.Watched),
            list = viewModel.watched.collectAsState().value,
            onAllClicked = onAllWatchedClicked,
            onMovieClicked = { movie -> onMovieClicked(movie) }
        )
        HorizontalRowWTitleSmall(
            title = stringResource(com.example.core.R.string.Dropped),
            list = viewModel.dropped.collectAsState().value,
            onAllClicked = onAllDroppedClicked,
            onMovieClicked = { movie -> onMovieClicked(movie) }
        )
    }
}