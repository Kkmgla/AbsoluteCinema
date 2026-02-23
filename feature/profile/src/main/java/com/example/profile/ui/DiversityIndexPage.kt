package com.example.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.R
import com.example.core.ui.LocalAccentColor

@Composable
fun DiversityIndexPage(
    diversityIndex: Double?,
    modifier: Modifier = Modifier,
) {
    val accentColor = LocalAccentColor.current
    var showInfoDialog by remember { mutableStateOf(false) }
    val infoContentDescription = stringResource(R.string.cd_info_diversity_index)
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        val titleWithIcon = buildAnnotatedString {
            append(stringResource(R.string.diversity_index_title))
            append("  ")
            appendInlineContent(id = "info_icon_inline", alternateText = "i")
        }
        val inlineContent = mapOf(
            "info_icon_inline" to InlineTextContent(
                placeholder = Placeholder(
                    width = 20.sp,
                    height = 20.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.TextCenter,
                ),
            ) {
                IconButton(
                    onClick = { showInfoDialog = true },
                    modifier = Modifier
                        .size(20.dp)
                        .semantics { contentDescription = infoContentDescription },
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_info),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp),
                    )
                }
            },
        )
        Text(
            text = titleWithIcon,
            inlineContent = inlineContent,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp),
        )
        if (showInfoDialog) {
            AlertDialog(
                onDismissRequest = { showInfoDialog = false },
                containerColor = MaterialTheme.colorScheme.surface,
                title = {
                    Text(
                        text = stringResource(R.string.diversity_index_info_dialog_title),
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
                            text = stringResource(R.string.diversity_index_info_explanation),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 12.dp),
                        )
                        Text(
                            text = stringResource(R.string.diversity_index_info_formula),
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 12.dp),
                        )
                        Text(
                            text = stringResource(R.string.diversity_index_info_where),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 12.dp),
                        )
                        Text(
                            text = stringResource(R.string.diversity_index_info_interpretation),
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 12.dp),
                        )
                        Text(
                            text = stringResource(R.string.diversity_index_info_example),
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
                            text = stringResource(R.string.diversity_index_info_close),
                            color = accentColor,
                            fontSize = 16.sp,
                        )
                    }
                },
            )
        }
        if (diversityIndex == null) {
            Text(
                text = stringResource(R.string.not_enough_data_statistics),
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
            )
        } else {
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(R.string.diversity_index_score_label),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(R.string.diversity_index_score_value, diversityIndex),
                    fontSize = 18.sp,
                    color = accentColor,
                    fontWeight = FontWeight.Medium,
                )
            }
            val interpretationRes = when {
                diversityIndex < 1.5 -> R.string.diversity_index_interpretation_low
                diversityIndex <= 2.5 -> R.string.diversity_index_interpretation_medium
                else -> R.string.diversity_index_interpretation_high
            }
            Text(
                text = stringResource(interpretationRes),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}
