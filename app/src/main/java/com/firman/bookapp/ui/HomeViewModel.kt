package com.firman.bookapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.firman.core.data.Resource
import com.firman.core.domain.model.Book
import com.firman.core.domain.usecase.BookUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class HomeViewModel(private val bookUseCase: BookUseCase) : ViewModel() {

    private val _searchQuery = MutableStateFlow("android")

    // Versi 1: Menggunakan Flow API langsung
    @OptIn(ExperimentalCoroutinesApi::class)
    val booksFlow = _searchQuery.flatMapLatest { query ->
        bookUseCase.searchBooks(query)
    }

    // Versi 2: Menggunakan LiveData transformation alternatif
    private val _books = MutableLiveData<Resource<List<Book>>>()
    val books: LiveData<Resource<List<Book>>> = _books

    init {
        searchBooks("android")
    }

    fun searchBooks(query: String) {
        viewModelScope.launch {
            _searchQuery.value = query
            bookUseCase.searchBooks(query).collect {
                _books.value = it
            }
        }
    }
}