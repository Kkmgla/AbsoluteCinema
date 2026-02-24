package com.example.profile.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.core.ui.LocalAccentColor
import com.example.domain.statistics.CompletionRate

private const val RING_SIZE_DP = 120f
private const val STROKE_WIDTH_DP = 12f
private const val START_ANGLE_DEG = 270f // 12 o'clock, clockwise = completion

@Composable
fun CompletionRatePage(
    completionRate: CompletionRate?,
    modifier: Modifier = Modifier,
) {
    val accentColor = LocalAccentColor.current
    var showInfoDialog by remember { mutableStateOf(false) }
    val infoContentDescription = stringResource(R.string.cd_info_completion_rate)
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
                    text = stringResource(R.string.completion_rate_title),
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
        }
        if (showInfoDialog) {
            AlertDialog(
                onDismissRequest = { showInfoDialog = false },
                containerColor = MaterialTheme.colorScheme.surface,
                title = {
                    Text(
                        text = stringResource(R.string.completion_rate_info_dialog_title),
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
                            text = stringResource(R.string.completion_rate_info_explanation),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 12.dp),
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.completion_rate_info_formula),
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(16.dp),
                            )
                        }
                        Text(
                            text = stringResource(R.string.completion_rate_info_where),
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
                            text = stringResource(R.string.completion_rate_info_close),
                            color = accentColor,
                            fontSize = 16.sp,
                        )
                    }
                },
            )
        }
        if (completionRate == null) {
            Text(
                text = stringResource(R.string.not_enough_data_statistics),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
            )
        } else {
            val animatedProgress by animateFloatAsState(
                targetValue = completionRate.completionRatio.toFloat(),
                animationSpec = tween(durationMillis = 600),
                label = "completion_ring",
            )
            val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(
                    modifier = Modifier.size(RING_SIZE_DP.dp),
                ) {
                    val strokeWidth = STROKE_WIDTH_DP.dp.toPx()
                    val radius = (size.minDimension / 2f) - strokeWidth / 2f
                    val center = Offset(size.width / 2f, size.height / 2f)
                    drawCircle(
                        color = surfaceVariant,
                        radius = radius,
                        center = center,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    )
                    drawArc(
                        color = accentColor,
                        startAngle = START_ANGLE_DEG,
                        sweepAngle = 360f * animatedProgress,
                        useCenter = false,
                        topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
                        size = Size(
                            size.width - strokeWidth,
                            size.height - strokeWidth,
                        ),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    )
                }
                Text(
                    text = stringResource(R.string.completion_rate_percent_center, completionRate.completionPercent),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = accentColor,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Text(
                    text = stringResource(R.string.completion_rate_watched, completionRate.watchedCount),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(R.string.completion_rate_dropped, completionRate.droppedCount),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }
    }
}
