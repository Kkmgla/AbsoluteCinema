package com.example.profile.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.core.ui.LocalAccentColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit


private val ACCENT_COLOR_OPTIONS = listOf(
    "#FF8000",
    "#CD4A4C",
    "#9B2D30",
    "#FF8C42",
    "#FFB84C",
    "#F26B38",
    "#5D76CB",
    "#4169E1"
)

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    onThemeChanged: (Boolean) -> Unit,
    onBackClicked: () -> Unit = {},
    currentAccentHex: String = "#FF8000",
    onAccentColorChanged: (String) -> Unit = {},
) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE) }
    val isDarkTheme = remember { mutableStateOf(sharedPreferences.getBoolean("dark_theme", false)) }
    val accentColor = LocalAccentColor.current


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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Настройки",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
            Text(
                text = "Назад",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                modifier = Modifier.clickable { onBackClicked.invoke() }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(bottom = 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    1.dp,
                    accentColor,
                    RoundedCornerShape(20.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .clickable {

                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Выбрать язык", fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                TextButton(
                    onClick = {

                    }
                ) {
                    Text("Русский", fontSize = 20.sp, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(bottom = 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    1.dp,
                    accentColor,
                    RoundedCornerShape(20.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text("Стиль темы", fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                Switch(
                    checked = isDarkTheme.value,
                    onCheckedChange = { checked ->
                        isDarkTheme.value = checked
                        sharedPreferences.edit { putBoolean("dark_theme", checked) }
                        onThemeChanged(checked)
                    },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = accentColor,
                        checkedThumbColor = colorResource(com.example.core.R.color.white)
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .padding(bottom = 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    1.dp,
                    accentColor,
                    RoundedCornerShape(20.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(com.example.core.R.string.main_color),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = currentAccentHex.uppercase(),
                    fontSize = 18.sp,
                    color = accentColor,
                    modifier = Modifier.clickable {
                        val index = ACCENT_COLOR_OPTIONS.indexOfFirst { it.equals(currentAccentHex, ignoreCase = true) }.takeIf { it >= 0 } ?: 0
                        val nextHex = ACCENT_COLOR_OPTIONS[(index + 1) % ACCENT_COLOR_OPTIONS.size]
                        onAccentColorChanged(nextHex)
                    }
                )
            }
        }
    }
}

