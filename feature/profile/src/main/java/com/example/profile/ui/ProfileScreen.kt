package com.example.profile.ui

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import coil3.compose.AsyncImage
import com.example.auth.viewmodel.AuthViewModel
import com.example.core.ui.LocalAccentColor
import java.io.File
import java.io.FileOutputStream

private const val AVATAR_PREFS = "profile_prefs"
private const val AVATAR_PATH_KEY = "avatar_path"
private const val AVATAR_FILE_NAME = "avatar.jpg"

private fun avatarFile(context: Context) = File(context.filesDir, AVATAR_FILE_NAME)

private fun loadAvatarPath(context: Context): String? {
    val path = context.getSharedPreferences(AVATAR_PREFS, Context.MODE_PRIVATE)
        .getString(AVATAR_PATH_KEY, null) ?: return null
    return path.takeIf { File(it).exists() }
}

private fun saveAvatarFromUri(context: Context, uri: android.net.Uri): String? {
    val dest = avatarFile(context)
    return try {
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(dest).use { output -> input.copyTo(output) }
        }
        context.getSharedPreferences(AVATAR_PREFS, Context.MODE_PRIVATE).edit {
            putString(AVATAR_PATH_KEY, dest.absolutePath)
        }
        dest.absolutePath
    } catch (_: Exception) {
        null
    }
}

@Composable
fun ProfileScreen(
    paddingValues: PaddingValues = PaddingValues(),
    onSettingsClicked: () -> Unit = {},
    onLogOut: () -> Unit,
    viewmodel: AuthViewModel
) {
    val user = viewmodel.getUser()
    var showLogoutDialog by remember { mutableStateOf(false) }
    val accentColor = LocalAccentColor.current
    val context = LocalContext.current
    var avatarFilePath by remember { mutableStateOf(loadAvatarPath(context)) }
    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: android.net.Uri? ->
        uri?.let {
            saveAvatarFromUri(context, it)?.let { path -> avatarFilePath = path }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize().padding(paddingValues)
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                )
                .border(
                    1.dp,
                    accentColor,
                    RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 40.dp)
                        .clickable { pickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarFilePath != null) {
                        AsyncImage(
                            model = File(avatarFilePath!!),
                            contentDescription = stringResource(com.example.core.R.string.cd_change_avatar),
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(accentColor),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Outlined.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier
                                .background(
                                    color = accentColor, shape = CircleShape
                                )
                                .size(100.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 4.dp, y = 4.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, accentColor, CircleShape)
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = stringResource(com.example.core.R.string.cd_change_avatar),
                            modifier = Modifier.size(18.dp),
                            tint = accentColor
                        )
                    }
                }
                val displayName = user?.displayName?.takeIf { it.isNotBlank() }
                val email = user?.email ?: stringResource(com.example.core.R.string.mockemail)
                Text(
                    text = displayName ?: email,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp),
                    fontSize = 18.sp
                )
                if (displayName != null && !user?.email.isNullOrBlank()) {
                    Text(
                        text = user?.email ?: "",
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
                        fontSize = 14.sp
                    )
                } else {
                    Box(modifier = Modifier.padding(bottom = 16.dp))
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp)
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
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .clickable {
                            onSettingsClicked.invoke()
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(com.example.core.R.string.settings), fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(40.dp)
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = accentColor
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .clickable {

                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(com.example.core.R.string.support), fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(40.dp)
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = accentColor
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .clickable {

                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(com.example.core.R.string.about_app), fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
        TextButton(
            onClick = {
                showLogoutDialog = true
            }
        ) {
            Text(stringResource(com.example.core.R.string.logout), fontSize = 20.sp, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.padding(bottom = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(com.example.core.R.string.version_app),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = stringResource(com.example.core.R.string.version_api),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = MaterialTheme.colorScheme.surface,
            text = {
                Text(
                    text = stringResource(com.example.core.R.string.action_confirmation),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewmodel.logOut()
                        onLogOut.invoke()
                    },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(com.example.core.R.string.confirm),
                        color = accentColor,
                        fontSize = 16.sp
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(com.example.core.R.string.cancel),
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp
                    )
                }
            }
        )
    }
}