package com.firman.bookapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.firman.core.domain.model.Book
import com.firman.core.domain.usecase.BookUseCase
import kotlinx.coroutines.launch

class DetailViewModel(private val bookUseCase: BookUseCase) : ViewModel() {

    fun getBookDetail(bookId: String) = bookUseCase.getBookDetail(bookId).asLiveData()

    fun setFavoriteBook(book: Book, newStatus: Boolean) {
        viewModelScope.launch {
            bookUseCase.setFavoriteBook(book, newStatus)
        }
    }

    fun isFavorite(bookId: String): LiveData<Boolean> {
        return bookUseCase.isFavorite(bookId).asLiveData()
    }
}