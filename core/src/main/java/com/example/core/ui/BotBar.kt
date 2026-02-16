package com.example.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.core.R
import com.example.core.ui.LocalAccentColor

enum class BotBarState {
    Home, Search, Users, Profile
}

@Preview
@Composable
fun BotBar(
    onHome: () -> Unit = {},
    onSearch: () -> Unit = {},
    onUsers: () -> Unit = {},
    onProfile: () -> Unit = {},
    selectedTab: BotBarState = BotBarState.Home,
) {
    val accentColor = LocalAccentColor.current
    fun isSelected(state: BotBarState): Boolean = selectedTab == state
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.secondary,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = { onHome.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "home",
                    tint = if (isSelected(BotBarState.Home)) accentColor else MaterialTheme.colorScheme.secondary
                )
            }
            IconButton(onClick = { onSearch.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "search",
                    tint = if (isSelected(BotBarState.Search)) accentColor else MaterialTheme.colorScheme.secondary
                )
            }
            IconButton(onClick = { onUsers.invoke() }) {
                Icon(
                    painter = painterResource(R.drawable.bookmark),
                    contentDescription = "favorite",
                    tint = if (isSelected(BotBarState.Users)) accentColor else MaterialTheme.colorScheme.secondary
                )
            }
            IconButton(onClick = { onProfile.invoke() }) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "user",
                    tint = if (isSelected(BotBarState.Profile)) accentColor else MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}