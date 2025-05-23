package com.firman.core.domain.model

data class Book(
    val id: String,
    val title: String?,
    val authors: String,
    val coverUrl: String?,
    val description: String?,
    val publishYear: Int?,
    val isFavorite: Boolean = false
)