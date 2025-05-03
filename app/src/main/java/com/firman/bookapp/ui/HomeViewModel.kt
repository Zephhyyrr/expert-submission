package com.firman.bookapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import com.firman.core.data.Resource
import com.firman.core.domain.model.Book
import com.firman.core.domain.usecase.BookUseCase

class HomeViewModel(private val bookUseCase: BookUseCase) : ViewModel() {

    private val _searchQuery = MutableLiveData<String>("android")

    val books: LiveData<Resource<List<Book>>> = _searchQuery.switchMap { query ->
        bookUseCase.searchBooks(query).asLiveData()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
