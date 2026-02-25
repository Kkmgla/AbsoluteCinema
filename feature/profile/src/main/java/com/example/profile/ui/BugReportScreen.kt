package com.example.profile.ui

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.LocalAccentColor

private const val BUG_REPORT_EMAIL = "developer@example.com"

private enum class BugType(val stringRes: Int) {
    VisualUi(com.example.core.R.string.bug_type_visual_ui),
    DataMismatch(com.example.core.R.string.bug_type_data_mismatch),
    Crash(com.example.core.R.string.bug_type_crash),
    Other(com.example.core.R.string.bug_type_other)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BugReportScreen(
    paddingValues: PaddingValues = PaddingValues(),
    onBackClicked: () -> Unit = {}
) {
    val context = LocalContext.current
    val accentColor = LocalAccentColor.current
    var reportText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(BugType.Other) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(paddingValues)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(com.example.core.R.string.bug_report_screen_title),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
            Text(
                text = stringResource(com.example.core.R.string.back),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                modifier = Modifier.clickable { onBackClicked() }
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = stringResource(selectedType.stringRes),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(20.dp)
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BugType.values().forEach { type ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(type.stringRes),
                                    fontSize = 16.sp
                                )
                            },
                            onClick = {
                                selectedType = type
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.heightIn(16.dp))

            OutlinedTextField(
                value = reportText,
                onValueChange = { reportText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                placeholder = {
                    Text(
                        text = stringResource(com.example.core.R.string.bug_report_placeholder),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                maxLines = 8,
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(modifier = Modifier.heightIn(24.dp))
        }

        Button(
            onClick = {
                val appName = context.applicationInfo.loadLabel(context.packageManager).toString()
                val subject = "$appName Bug Report - ${context.getString(selectedType.stringRes)}"
                val versionName = try {
                    context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "?"
                } catch (_: Exception) {
                    "?"
                }
                val metadata = """
                    ---
                    App: $appName
                    App version: $versionName
                    Android: ${Build.VERSION.RELEASE}
                    Device: ${Build.MODEL}
                    """.trimIndent()
                val body = reportText.trim() + "\n\n" + metadata

                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = android.net.Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(BUG_REPORT_EMAIL))
                    putExtra(Intent.EXTRA_SUBJECT, subject)
                    putExtra(Intent.EXTRA_TEXT, body)
                }
                context.startActivity(Intent.createChooser(intent, context.getString(com.example.core.R.string.send_report)))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            enabled = reportText.isNotBlank()
        ) {
            Text(stringResource(com.example.core.R.string.send_report))
        }
    }
}
