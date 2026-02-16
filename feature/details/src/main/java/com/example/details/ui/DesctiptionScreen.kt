package com.example.details.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.core.ui.LocalAccentColor
import com.example.core.util.getName
import com.example.details.viewmodel.DetailsViewModel

private val DESCRIPTION_CORNER_RADIUS = 20.dp
private val DESCRIPTION_BORDER_WIDTH = 1.dp
private val DESCRIPTION_BORDER_SHAPE = RoundedCornerShape(DESCRIPTION_CORNER_RADIUS)

@Composable
fun DescriptionScreen(
    paddingValues: PaddingValues,
    viewModel: DetailsViewModel,
    onBackClicked: () -> Unit = {},
) {
    val movie by viewModel.movie.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(DESCRIPTION_BORDER_SHAPE)
                .background(MaterialTheme.colorScheme.surface, DESCRIPTION_BORDER_SHAPE)
                .border(DESCRIPTION_BORDER_WIDTH, LocalAccentColor.current, DESCRIPTION_BORDER_SHAPE)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = movie.getName(),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                movie.shortDescription?.takeIf { it.isNotBlank() }?.let { short ->
                    Text(
                        text = short,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 12.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    val fullDescription = movie.description?.takeIf { it.isNotBlank() }
                        ?: movie.shortDescription?.takeIf { it.isNotBlank() }
                    Text(
                        text = fullDescription ?: "Описание отсутствует",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 15.sp
                    )
                }
                TextButton(
                    onClick = onBackClicked,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Закрыть",
                        color = LocalAccentColor.current
                    )
                }
            }
        }
    }
}
