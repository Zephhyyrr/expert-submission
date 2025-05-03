package com.firman.bookapp.di

import com.firman.bookapp.ui.HomeViewModel
import com.firman.bookapp.ui.detail.DetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}