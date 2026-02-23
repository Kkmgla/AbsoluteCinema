package com.example.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.core.ui.LocalAccentColor
import com.example.profile.viewmodel.StatisticsViewModel

private val STATISTICS_PAGE_KEYS = listOf(
    "genres",
    "temporal_and_diversity",
    "ratings_combined",
    "completion_rate",
)

private const val FAVORITE_GENRES_HEADER_HEIGHT_DP = 68f
private const val FAVORITE_GENRES_ROW_HEIGHT_DP = 46f
private const val STATISTICS_CARD_PADDING_TOTAL_DP = 48f
private val STATISTICS_CARD_MIN_HEIGHT_DP: Dp = 200.dp
private val TEMPORAL_BLOCK_MIN_HEIGHT_DP: Dp = 220.dp
private val DIVERSITY_BLOCK_MIN_HEIGHT_DP: Dp = 180.dp
private val TEMPORAL_PLUS_DIVERSITY_PAGE_MIN_HEIGHT_DP: Dp =
    TEMPORAL_BLOCK_MIN_HEIGHT_DP + DIVERSITY_BLOCK_MIN_HEIGHT_DP + 32.dp
private val RATINGS_PAGE_MIN_HEIGHT_DP: Dp =
    STATISTICS_CARD_MIN_HEIGHT_DP + STATISTICS_CARD_MIN_HEIGHT_DP + STATISTICS_CARD_MIN_HEIGHT_DP + 32.dp
private val COMPLETION_RATE_PAGE_MIN_HEIGHT_DP: Dp = 320.dp
private val STATISTICS_CARD_HORIZONTAL_GAP_DP = 8.dp

private fun computeCardHeightDp(genreCount: Int): Dp {
    val contentHeight = FAVORITE_GENRES_HEADER_HEIGHT_DP +
        (genreCount * FAVORITE_GENRES_ROW_HEIGHT_DP).toFloat()
    val total = contentHeight + STATISTICS_CARD_PADDING_TOTAL_DP
    val minPx = STATISTICS_CARD_MIN_HEIGHT_DP
    return maxOf(total.dp, minPx, TEMPORAL_BLOCK_MIN_HEIGHT_DP)
}

private fun computePagerHeightDp(genreCount: Int): Dp {
    return maxOf(
        computeCardHeightDp(genreCount),
        TEMPORAL_PLUS_DIVERSITY_PAGE_MIN_HEIGHT_DP,
        RATINGS_PAGE_MIN_HEIGHT_DP,
        COMPLETION_RATE_PAGE_MIN_HEIGHT_DP,
        TEMPORAL_BLOCK_MIN_HEIGHT_DP,
        DIVERSITY_BLOCK_MIN_HEIGHT_DP,
        STATISTICS_CARD_MIN_HEIGHT_DP,
    )
}

@Composable
fun StatisticsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    viewModel: StatisticsViewModel,
    onBackClicked: () -> Unit = {},
) {
    val topGenreScores by viewModel.topGenreScores.collectAsState()
    val temporalBalance by viewModel.temporalBalance.collectAsState()
    val diversityIndex by viewModel.diversityIndex.collectAsState()
    val averageRating by viewModel.averageRating.collectAsState()
    val ratingVariance by viewModel.ratingVariance.collectAsState()
    val ratingBias by viewModel.ratingBias.collectAsState()
    val completionRate by viewModel.completionRate.collectAsState()
    val accentColor = LocalAccentColor.current
    val pagerState = rememberPagerState(pageCount = { STATISTICS_PAGE_KEYS.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .padding(horizontal = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.statistics),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
            )
            Text(
                text = "Назад",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                modifier = Modifier.clickable { onBackClicked.invoke() },
            )
        }

        if (topGenreScores == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.not_enough_data_statistics),
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 16.sp,
                )
            }
        } else {
            val genres = topGenreScores!!
            StatisticsPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(computePagerHeightDp(genres.size)),
                pageCount = STATISTICS_PAGE_KEYS.size,
                pagerState = pagerState,
                showDotsInside = false,
                pageContent = { page ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = STATISTICS_CARD_HORIZONTAL_GAP_DP),
                    ) {
                        when (STATISTICS_PAGE_KEYS.getOrNull(page)) {
                            "genres" -> StatisticsCard(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                                FavoriteGenresPage(genreScores = genres)
                            }

                            "temporal_and_diversity" -> Column(
                                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                            ) {
                                StatisticsCard(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                                    TemporalBalancePage(temporalBalance = temporalBalance)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                StatisticsCard(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                                    DiversityIndexPage(diversityIndex = diversityIndex)
                                }
                            }

                            "ratings_combined" -> Column(
                                modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                            ) {
                                StatisticsCard(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                                    RatingStatsPage(averageRating = averageRating)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                StatisticsCard(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                                    RatingConsistencyPage(ratingVariance = ratingVariance)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                StatisticsCard(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                                    RatingBiasPage(ratingBias = ratingBias)
                                }
                            }

                            "completion_rate" -> StatisticsCard(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                                CompletionRatePage(completionRate = completionRate)
                            }

                            else -> StatisticsCard(modifier = Modifier.fillMaxWidth().wrapContentHeight()) {
                                FavoriteGenresPage(genreScores = genres)
                            }
                        }
                    }
                },
            )
            Spacer(modifier = Modifier.height(40.dp))
            PaginationDots(
                pageCount = STATISTICS_PAGE_KEYS.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )
        }
    }
}
