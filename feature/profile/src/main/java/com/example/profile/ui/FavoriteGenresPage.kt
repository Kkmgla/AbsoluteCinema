package com.example.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.core.ui.LocalAccentColor
import com.example.domain.statistics.GenreScore

@Composable
fun FavoriteGenresPage(
    genreScores: List<GenreScore>?,
    modifier: Modifier = Modifier,
) {
    val accentColor = LocalAccentColor.current
    var showAsPercentage by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    val infoContentDescription = stringResource(R.string.cd_info_favorite_genres)
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(R.string.favorite_genres),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary,
                )
                IconButton(
                    onClick = { showInfoDialog = true },
                    modifier = Modifier
                        .size(24.dp)
                        .semantics { contentDescription = infoContentDescription },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_info),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
            if (!genreScores.isNullOrEmpty()) {
                val toggleContentDescription = if (showAsPercentage) {
                    stringResource(R.string.cd_show_scores)
                } else {
                    stringResource(R.string.cd_show_percentages)
                }
                IconButton(
                    onClick = { showAsPercentage = !showAsPercentage },
                    modifier = Modifier
                        .size(32.dp)
                        .semantics { contentDescription = toggleContentDescription },
                ) {
                    Text(
                        text = "%",
                        fontSize = 20.sp,
                        color = if (showAsPercentage) accentColor else MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
        if (showInfoDialog) {
            AlertDialog(
                onDismissRequest = { showInfoDialog = false },
                containerColor = MaterialTheme.colorScheme.surface,
                title = {
                    Text(
                        text = stringResource(R.string.favorite_genres_info_dialog_title),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                    )
                },
                text = {
                    Column(
                        modifier = Modifier
                            .heightIn(max = 400.dp)
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Text(
                            text = stringResource(R.string.favorite_genres_info_intro),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 12.dp),
                        )
                        Text(
                            text = stringResource(R.string.favorite_genres_info_formula_label),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 4.dp),
                        )
                        Text(
                            text = stringResource(R.string.favorite_genres_info_formula),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 12.dp),
                        )
                        Text(
                            text = stringResource(R.string.favorite_genres_info_where),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp),
                        )
                        Text(
                            text = stringResource(R.string.favorite_genres_info_movie_rating),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 6.dp),
                        )
                        Text(
                            text = stringResource(R.string.favorite_genres_info_genre_indicator),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 6.dp),
                        )
                        Text(
                            text = stringResource(R.string.favorite_genres_info_n),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 12.dp),
                        )
                        Text(
                            text = stringResource(R.string.favorite_genres_info_explanation_label),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 6.dp),
                        )
                        Text(
                            text = stringResource(R.string.favorite_genres_info_explanation),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = { showInfoDialog = false },
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.favorite_genres_info_close),
                            color = accentColor,
                            fontSize = 16.sp,
                        )
                    }
                },
            )
        }
        if (genreScores == null || genreScores.isEmpty()) {
            Text(
                text = stringResource(R.string.not_enough_data_statistics),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
            )
        } else {
            val totalScore = genreScores.sumOf { it.score }
            val showPercent = showAsPercentage && totalScore > 0
            genreScores.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = item.genreName,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = if (showPercent) {
                            "— %.1f%%".format((item.score / totalScore) * 100)
                        } else {
                            "— %.2f".format(item.score)
                        },
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
                if (index < genreScores.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp),
                        color = accentColor,
                    )
                }
            }
        }
    }
}
