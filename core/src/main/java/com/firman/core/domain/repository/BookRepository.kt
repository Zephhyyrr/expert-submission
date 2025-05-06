package com.firman.core.domain.repository

import com.firman.core.data.Resource
import com.firman.core.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun searchBooks(query: String): Flow<Resource<List<Book>>>
    fun getBookDetail(bookId: String): Flow<Resource<Book>>
    fun getFavoriteBooks(): Flow<List<Book>>
    suspend fun setFavoriteBook(book: Book, state: Boolean)
    fun isFavorite(bookId: String): Flow<Boolean>
}