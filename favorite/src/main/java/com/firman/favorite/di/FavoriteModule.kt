package com.firman.favorite.di

import com.firman.favorite.ui.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val favoriteModule = module {
    viewModel { FavoriteViewModel(get()) }
}
