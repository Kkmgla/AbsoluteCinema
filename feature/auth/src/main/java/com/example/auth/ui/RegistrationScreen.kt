package com.example.auth.ui


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusRequester
import com.example.auth.viewmodel.AuthState
import com.example.auth.viewmodel.AuthViewModel
import com.example.core.R

@Composable
fun RegistrationScreen(
    onToLogin: () -> Unit = {},
    viewModel: AuthViewModel,
    onSuccess: () -> Unit,
    context: Context,
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val confirmPasswordVisible = remember { mutableStateOf(false) }
    val authState = viewModel.authState.collectAsState()
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    fun submitRegistration() {
        viewModel.createAccount(
            email = email.value,
            password = password.value,
            confirmPassword = confirmPassword.value
        )
    }

    val isEmailValid = email.value.contains("@")
    val isPasswordValid = password.value.length >= 6
    val passwordsMatch = password.value == confirmPassword.value && confirmPassword.value.isNotEmpty()
    val canSubmit = isEmailValid && isPasswordValid && passwordsMatch

    if (authState.value is AuthState.CreateAccountFail) {
        Toast.makeText(
            context, (authState.value as AuthState.CreateAccountFail).message, Toast.LENGTH_LONG
        ).show()
    }

    if (authState.value is AuthState.SignInSuccess)
        onSuccess.invoke()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (authState.value is AuthState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        Box(
            modifier = Modifier.align(Alignment.Center)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                Text(
                    text = stringResource(R.string.Registration),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(12.dp)
                )

                OutlinedTextField(
                    value = email.value,
                    placeholder = { Text(stringResource(R.string.mockemail)) },
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .focusOrder(passwordFocusRequester),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),
                    keyboardActions = KeyboardActions(
                        onNext = { passwordFocusRequester.requestFocus() }
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            email.value = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.cd_clear_field)
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = colorResource(R.color.accent),
                        focusedIndicatorColor = colorResource(R.color.accent),
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                    ),
                    onValueChange = {
                        email.value = it
                    },
                    isError = authState.value is AuthState.CreateAccountFail

                )
                OutlinedTextField(
                    value = password.value,
                    placeholder = { Text(stringResource(R.string.Password)) },
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .focusRequester(passwordFocusRequester)
                        .focusOrder(confirmPasswordFocusRequester),
                    singleLine = true,
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(
                        onNext = { confirmPasswordFocusRequester.requestFocus() }
                    ),
                    trailingIcon = {
                        Row {
                            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                                Icon(
                                    imageVector = if (passwordVisible.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible.value) stringResource(R.string.cd_hide_password) else stringResource(R.string.cd_show_password)
                                )
                            }
                            IconButton(onClick = { password.value = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = stringResource(R.string.cd_clear_field)
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = colorResource(R.color.accent),
                        focusedIndicatorColor = colorResource(R.color.accent),
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                    ),
                    onValueChange = {
                        password.value = it
                    },
                    isError = authState.value is AuthState.CreateAccountFail
                )
                OutlinedTextField(
                    value = confirmPassword.value,
                    placeholder = { Text(stringResource(R.string.ConfirmPassword)) },
                    modifier = Modifier
                        .padding(bottom = 12.dp)
                        .focusRequester(confirmPasswordFocusRequester),
                    singleLine = true,
                    visualTransformation = if (confirmPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            submitRegistration()
                        }
                    ),
                    trailingIcon = {
                        Row {
                            IconButton(onClick = { confirmPasswordVisible.value = !confirmPasswordVisible.value }) {
                                Icon(
                                    imageVector = if (confirmPasswordVisible.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (confirmPasswordVisible.value) stringResource(R.string.cd_hide_password) else stringResource(R.string.cd_show_password)
                                )
                            }
                            IconButton(onClick = { confirmPassword.value = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = stringResource(R.string.cd_clear_field)
                                )
                            }
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = colorResource(R.color.accent),
                        focusedIndicatorColor = colorResource(R.color.accent),
                        unfocusedContainerColor = MaterialTheme.colorScheme.background,
                        focusedContainerColor = MaterialTheme.colorScheme.background,
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.secondary,
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.secondary,
                    ),
                    onValueChange = {
                        confirmPassword.value = it
                    },
                    isError = authState.value is AuthState.CreateAccountFail
                )
                if (confirmPassword.value.isNotEmpty() && password.value != confirmPassword.value) {
                    Text(
                        text = stringResource(R.string.validation_passwords_mismatch),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 4.dp),
                        fontSize = 12.sp
                    )
                }

                Row {
                    Text(
                        text = stringResource(R.string.HasAccount),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = stringResource(R.string.ToLogin),
                        color = colorResource(
                            R.color.accent
                        ),
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .clickable {
                                onToLogin.invoke()
                            })
                }
                }
            }
        }

        Button(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
            enabled = canSubmit,
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.accent),
                disabledContainerColor = colorResource(R.color.accent).copy(alpha = 0.5f)
            ),
            onClick = { submitRegistration() }) {
            Text(
                stringResource(R.string.Registrate),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )
        }
    }
}