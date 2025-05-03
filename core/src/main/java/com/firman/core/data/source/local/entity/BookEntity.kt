package com.firman.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book")
data class BookEntity(
    @PrimaryKey
    @ColumnInfo(name = "bookId")
    var bookId: String,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "authors")
    var authors: String,

    @ColumnInfo(name = "coverUrl")
    var coverUrl: String?,

    @ColumnInfo(name = "description")
    var description: String?,

    @ColumnInfo(name = "publishYear")
    var publishYear: Int?,

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
)