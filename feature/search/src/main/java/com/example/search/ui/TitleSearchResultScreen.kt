package com.example.search.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.CommonMovieListScreen
import com.example.core.ui.LocalAccentColor
import com.example.domain.model.Movie
import com.example.search.viewmodel.SearchState
import com.example.search.viewmodel.SearchViewModel

@Composable
fun TitleSearchResultScreen(
    query: String,
    viewModel: SearchViewModel,
    paddingValues: PaddingValues,
    onBackClicked: () -> Unit,
    onMovieClicked: (Movie) -> Unit,
) {
    val searchState by viewModel.searchState.collectAsState()

    LaunchedEffect(query) {
        if (query.isNotBlank()) {
            viewModel.searchWithDebounce(query = query, launchedFromButton = true)
        }
    }

    when (searchState) {
        SearchState.Idle -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Введите запрос и нажмите поиск",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        SearchState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }

        is SearchState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Проблемы с соединением",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )
                    TextButton(
                        onClick = {
                            viewModel.searchWithDebounce(
                                query = query,
                                launchedFromButton = true,
                            )
                        },
                    ) {
                        Text(
                            text = "Обновить",
                            color = LocalAccentColor.current,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }

        SearchState.Empty -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Ничего не найдено",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                    )
                    TextButton(onClick = { onBackClicked.invoke() }) {
                        Text(
                            text = "Назад",
                            color = LocalAccentColor.current,
                        )
                    }
                }
            }
        }

        is SearchState.Success -> {
            val movies = (searchState as SearchState.Success).movies
            CommonMovieListScreen(
                paddingValues = paddingValues,
                title = "Результаты поиска",
                moviesList = movies,
                onBackClicked = onBackClicked,
                onMovieClicked = onMovieClicked,
            )
        }
    }
}
