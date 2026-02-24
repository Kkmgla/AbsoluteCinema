package com.example.profile.ui

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.core.ui.AccentHexOptions
import com.example.core.ui.LocalAccentColor
import com.example.core.ui.ThemeStyle
import com.example.core.ui.nextCompatibleAccentHex
import com.example.core.ui.nextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    paddingValues: PaddingValues = PaddingValues(),
    currentThemeStyle: ThemeStyle,
    onThemeStyleChanged: (ThemeStyle) -> Unit,
    onBackClicked: () -> Unit = {},
    currentAccentHex: String = "#FF8000",
    onAccentColorChanged: (String) -> Unit = {},
) {
    val accentColor = LocalAccentColor.current
    val currentThemeStyleText = when (currentThemeStyle) {
        ThemeStyle.Light -> stringResource(com.example.core.R.string.theme_style_light)
        ThemeStyle.Dark -> stringResource(com.example.core.R.string.theme_style_dark)
        ThemeStyle.Ultramarine -> stringResource(com.example.core.R.string.theme_style_ultramarine)
        ThemeStyle.Graphite -> stringResource(com.example.core.R.string.theme_style_graphite)
        ThemeStyle.SoftLight -> stringResource(com.example.core.R.string.theme_style_soft_light)
    }


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
                    .clickable { },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Выбрать язык", fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                Text(
                    text = "Русский",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    modifier = Modifier.clickable { }
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
                    stringResource(com.example.core.R.string.theme_style_label),
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = currentThemeStyleText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    modifier = Modifier.clickable {
                        onThemeStyleChanged(currentThemeStyle.nextStyle())
                    },
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
                    fontWeight = FontWeight.Bold,
                    color = accentColor,
                    modifier = Modifier.clickable {
                        val nextHex = nextCompatibleAccentHex(
                            currentAccentHex = currentAccentHex,
                            themeStyle = currentThemeStyle,
                            options = AccentHexOptions,
                        )
                        onAccentColorChanged(nextHex)
                    }
                )
            }
        }
    }
}

