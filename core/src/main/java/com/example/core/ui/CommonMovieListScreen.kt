package com.example.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.core.ui.LocalAccentColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.domain.model.Movie

@Preview
@Composable
fun CommonMovieListScreen(
    paddingValues: PaddingValues = PaddingValues(),
    title: String = "Title",
    moviesList: List<Movie> = emptyList(),
    onBackClicked: () -> Unit = {},
    onMovieClicked: (Movie) -> Unit = {},
) {
    var isGridMode by rememberSaveable { mutableStateOf(false) }
    val accentColor = LocalAccentColor.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isGridMode) R.drawable.ic_list else R.drawable.ic_grid
                        ),
                        contentDescription = if (isGridMode) "Список" else "Сетка",
                        tint = accentColor,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { isGridMode = !isGridMode }
                    )
                    Text(
                        text = "Назад",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        modifier = Modifier.clickable {
                            onBackClicked.invoke()
                        }
                    )
                }
            }
        }
        if (isGridMode) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(moviesList) { movie ->
                    CommonMoviePosterGridItem(
                        movie = movie,
                        onMovieClicked = onMovieClicked
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                items(moviesList) {
                    CommonMovieItem(
                        movie = it,
                        onMovieClicked = onMovieClicked
                    )
                }
            }
        }
    }
}