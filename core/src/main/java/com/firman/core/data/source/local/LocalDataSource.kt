package com.firman.core.data.source.local

import com.firman.core.data.source.local.entity.BookEntity
import com.firman.core.data.source.local.room.BookDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val bookDao: BookDao) {
    fun getFavoriteBooks(): Flow<List<BookEntity>> = bookDao.getFavoriteBooks()

    suspend fun insertBook(book: BookEntity) = bookDao.insertBook(book)

    suspend fun setFavoriteBook(bookId: String, isFavorite: Boolean) =
        bookDao.updateFavoriteBook(bookId, isFavorite)

    fun isFavorite(bookId: String): Flow<Boolean> = bookDao.isFavorite(bookId)
}