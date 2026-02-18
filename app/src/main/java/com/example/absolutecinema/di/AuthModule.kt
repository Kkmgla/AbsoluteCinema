package com.example.absolutecinema.di

import com.example.auth.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<FirebaseAuth> {
        // Use getInstance() which is more reliable than Firebase.auth
        // It will use the default FirebaseApp instance
        FirebaseAuth.getInstance()
    }

    viewModel<AuthViewModel> {
        AuthViewModel(
            auth = get() // Use injected FirebaseAuth instance
        )
    }
}