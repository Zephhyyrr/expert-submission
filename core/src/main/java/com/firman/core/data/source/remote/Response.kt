package com.firman.core.data.source.remote

data class SearchResponse(
    val numFound: Int,
    val start: Int,
    val docs: List<BookResponse>?
)

data class BookResponse(
    val key: String,
    val title: String,
    val author_name: List<String>?,
    val cover_i: Int?,
    val first_publish_year: Int?
)

data class BookDetailResponse(
    val key: String,
    val title: String,
    val cover_id: String?,
    val first_publish_year: Int?,
    val description: String?,
    val authors: List<AuthorReference>?
)

data class AuthorReference(
    val author: Author
)

data class Author(
    val key: String,
    val name: String
)