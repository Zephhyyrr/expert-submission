package com.firman.favorite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.firman.core.domain.usecase.BookUseCase

class FavoriteViewModel(private val bookUseCase: BookUseCase) : ViewModel() {
    val favoriteBooks = bookUseCase.getFavoriteBooks().asLiveData()
}
