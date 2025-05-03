package com.firman.favorite.di

import com.firman.favorite.ui.FavoriteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module

object FavoriteModuleProvider {

    private var isLoaded = false

    fun loadFavoriteModule() {
        if (!isLoaded) {
            loadKoinModules(favoriteModule)
            isLoaded = true
        }
    }

    private val favoriteModule: Module = module {
        viewModel { FavoriteViewModel(get()) }
    }
}
