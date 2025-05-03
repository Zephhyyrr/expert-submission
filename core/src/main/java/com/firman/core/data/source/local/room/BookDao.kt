package com.firman.core.data.source.local.room

import androidx.room.*
import com.firman.core.data.source.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("SELECT * FROM book WHERE isFavorite = 1")
    fun getFavoriteBooks(): Flow<List<BookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookEntity)

    @Query("UPDATE book SET isFavorite = :isFavorite WHERE bookId = :bookId")
    suspend fun updateFavoriteBook(bookId: String, isFavorite: Boolean)

    @Query("SELECT EXISTS(SELECT * FROM book WHERE bookId = :bookId AND isFavorite = 1)")
    fun isFavorite(bookId: String): Flow<Boolean>
}