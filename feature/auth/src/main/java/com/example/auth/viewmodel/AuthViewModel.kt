package com.example.auth.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AuthViewModel(
    private val auth: FirebaseAuth,
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    fun getUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun checkUser(): Boolean = auth.currentUser != null

    private suspend fun signInWithEmail(email: String, password: String): AuthResult {
        return suspendCoroutine { continuation ->
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result!!)
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Unknown error")
                        )
                    }
                }
        }
    }

    private fun friendlyErrorMessage(e: Exception): String {
        val msg = (e.message ?: "").trim()
        return when {
            msg.contains("network error", ignoreCase = true) || 
            msg.contains("timeout", ignoreCase = true) || 
            msg.contains("interrupted connection", ignoreCase = true) || 
            msg.contains("unreachable", ignoreCase = true) ||
            msg.contains("Unable to resolve host", ignoreCase = true) ||
            msg.contains("failed to connect", ignoreCase = true) -> 
                "Проблемы с соединением. Проверьте интернет и попробуйте снова."
            msg.contains("password is invalid", ignoreCase = true) || 
            msg.contains("wrong password", ignoreCase = true) -> 
                "Неверный пароль. Проверьте правильность ввода."
            msg.contains("no user record", ignoreCase = true) || 
            msg.contains("user not found", ignoreCase = true) -> 
                "Пользователь с таким email не найден."
            msg.contains("email address is already in use", ignoreCase = true) -> 
                "Email уже используется. Войдите в аккаунт или используйте другой email."
            msg.contains("email address is badly formatted", ignoreCase = true) || 
            msg.contains("invalid email", ignoreCase = true) -> 
                "Некорректный email адрес."
            msg.contains("weak password", ignoreCase = true) || 
            msg.contains("password should be at least", ignoreCase = true) -> 
                "Пароль слишком слабый. Используйте минимум 6 символов."
            msg.contains("too many requests", ignoreCase = true) -> 
                "Слишком много попыток. Попробуйте позже."
            msg.isNotBlank() -> msg
            else -> "Произошла ошибка. Попробуйте снова."
        }
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
        _authState.value = AuthState.Loading
        try {
            val result = signInWithEmail(email, password)
            _authState.value = AuthState.SignInSuccess(result.user!!)
        } catch (e: Exception) {
            _authState.value = AuthState.SignInFail(friendlyErrorMessage(e))
        }
    }

    private suspend fun register(email: String, password: String): AuthResult {
        return suspendCoroutine { continuation ->
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(task.result!!)
                    } else {
                        continuation.resumeWithException(
                            task.exception ?: Exception("Unknown error")
                        )
                    }
                }
        }
    }

    fun createAccount(email: String, password: String, confirmPassword: String) =
        viewModelScope.launch {
            if (password != confirmPassword) {
                _authState.value = AuthState.CreateAccountFail("Пароли не совпадают")
                return@launch
            }

            _authState.value = AuthState.Loading
            try {
                val result = register(email, password)
                _authState.value = AuthState.SignInSuccess(result.user!!)
            } catch (e: Exception) {
                _authState.value = AuthState.CreateAccountFail(friendlyErrorMessage(e))
            }
        }

    fun logOut() {
        auth.signOut()
        _authState.value = AuthState.Idle
    }

    fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (email.isBlank() || !email.contains("@")) {
            onError("Введите корректный email")
            return
        }
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(friendlyErrorMessage(e)) }
    }
}