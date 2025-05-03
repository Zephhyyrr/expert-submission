package com.firman.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.firman.core.data.source.local.entity.BookEntity

@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}