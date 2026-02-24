package com.example.absolutecinema

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import androidx.core.content.edit
import com.example.core.ui.LocalAccentColor
import com.example.core.ui.ThemeStyle
import com.example.core.ui.normalizeAccentForTheme
import com.example.core.ui.parseAccentHex
import com.example.auth.viewmodel.AuthViewModel
import com.example.absolutecinema.navigtion.AppNavigation
import com.example.absolutecinema.ui.theme.AbsoluteCinemaTheme
import com.example.details.viewmodel.DetailsViewModel
import com.example.feed.viewmodel.FeedViewModel
import com.example.search.viewmodel.SearchViewModel
import com.example.profile.viewmodel.StatisticsViewModel
import com.example.users.viewmodel.UsersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val feedViewModel by viewModel<FeedViewModel>()
    private val detailsViewModel by viewModel<DetailsViewModel>()
    private val usersViewModel by viewModel<UsersViewModel>()
    private val searchViewModel by viewModel<SearchViewModel>()
    private val authViewModel by viewModel<AuthViewModel>()
    private val statisticsViewModel by viewModel<StatisticsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val prefs = remember { getSharedPreferences("theme_prefs", MODE_PRIVATE) }
            val hasThemeStyleKey = remember { prefs.contains("theme_style") }
            val legacyDarkTheme = remember { prefs.getBoolean("dark_theme", false) }
            var themeStyle by remember {
                mutableStateOf(
                    if (hasThemeStyleKey) {
                        ThemeStyle.fromStorageValue(prefs.getString("theme_style", null))
                    } else {
                        if (legacyDarkTheme) ThemeStyle.Dark else ThemeStyle.Light
                    }
                )
            }
            var accentHex by remember {
                mutableStateOf(
                    normalizeAccentForTheme(
                        preferredAccentHex = prefs.getString("accent_color_hex", "#FF8000") ?: "#FF8000",
                        themeStyle = themeStyle,
                    )
                )
            }
            LaunchedEffect(themeStyle) {
                val normalized = normalizeAccentForTheme(accentHex, themeStyle)
                if (!normalized.equals(accentHex, ignoreCase = true)) {
                    accentHex = normalized
                    prefs.edit { putString("accent_color_hex", normalized) }
                }
            }
            val accentColor = remember(accentHex) { parseAccentHex(accentHex) }

            CompositionLocalProvider(LocalAccentColor provides accentColor) {
                AbsoluteCinemaTheme(themeStyle = themeStyle) {
                    val navController = rememberNavController()

                    AppNavigation(
                        navController = navController,
                        feedViewModel = feedViewModel,
                        detailsViewModel = detailsViewModel,
                        usersViewModel = usersViewModel,
                        searchViewModel = searchViewModel,
                        authViewModel = authViewModel,
                        statisticsViewModel = statisticsViewModel,
                        currentThemeStyle = themeStyle,
                        onThemeStyleChanged = { nextStyle ->
                            themeStyle = nextStyle
                            prefs.edit {
                                putString("theme_style", nextStyle.storageValue)
                                putBoolean("dark_theme", nextStyle == ThemeStyle.Dark)
                            }
                        },
                        currentAccentHex = accentHex,
                        onAccentColorChanged = { hex ->
                            val normalized = normalizeAccentForTheme(hex, themeStyle)
                            accentHex = normalized
                            prefs.edit { putString("accent_color_hex", normalized) }
                        },
                        context = this
                    )
                }
            }
        }
    }
}

