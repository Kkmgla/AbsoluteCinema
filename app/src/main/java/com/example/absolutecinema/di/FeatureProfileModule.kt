package com.example.absolutecinema.di

import com.example.profile.viewmodel.StatisticsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val featureProfileModule = module {
    viewModel<StatisticsViewModel> {
        StatisticsViewModel(repository = get())
    }
}
