package com.firman.core.data.source.remote

import com.google.gson.JsonElement

data class SearchResponse(
    val numFound: Int,
    val start: Int,
    val docs: List<BookResponse> = emptyList()
)

data class BookResponse(
    val key: String,
    val title: String,
    val author_name: List<String>? = null,
    val cover_i: Int? = null,
    val first_publish_year: Int? = null
)

data class BookDetailResponse(
    val key: String,
    val title: String,
    val cover_id: Any? = null,
    val first_publish_year: Any? = null,
    val description: Any? = null,
    val authors: List<AuthorReference>? = null
)

data class AuthorReference(
    val author: Author? = null
)

data class Author(
    val key: String,
    val name: String
)