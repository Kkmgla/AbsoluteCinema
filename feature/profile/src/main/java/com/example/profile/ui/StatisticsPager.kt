package com.example.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatisticsPager(
    pageCount: Int,
    modifier: Modifier = Modifier,
    pagerState: PagerState? = null,
    showDotsInside: Boolean = true,
    pageContent: @Composable (page: Int) -> Unit,
) {
    if (pageCount <= 0) return
    val state = pagerState ?: rememberPagerState(pageCount = { pageCount })

    Column(modifier = modifier.fillMaxWidth().fillMaxHeight()) {
        HorizontalPager(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            userScrollEnabled = true,
        ) { page ->
            pageContent(page)
        }
        if (showDotsInside) {
            PaginationDots(
                pageCount = pageCount,
                currentPage = state.currentPage,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
            )
        }
    }
}
