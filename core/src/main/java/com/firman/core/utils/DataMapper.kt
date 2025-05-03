package com.firman.core.utils

import com.firman.core.data.source.local.entity.BookEntity
import com.firman.core.data.source.remote.BookDetailResponse
import com.firman.core.data.source.remote.BookResponse
import com.firman.core.domain.model.Book

object DataMapper {
    fun mapResponsesToDomain(input: List<BookResponse>): List<Book> {
        return input.map {
            Book(
                id = it.key.removePrefix("/works/"),
                title = it.title,
                authors = it.author_name ?: listOf("Unknown Author"),
                coverUrl = if (it.cover_i != null) "https://covers.openlibrary.org/b/id/${it.cover_i}-L.jpg" else null,
                description = null,
                publishYear = it.first_publish_year,
                isFavorite = false
            )
        }
    }

    fun mapResponseToDomain(
        input: BookDetailResponse,
        bookResponse: BookResponse?,
        isFavorite: Boolean = false
    ): Book {
        val authorsFromDetail = if (input.authors != null) {
            try {
                input.authors.mapNotNull { authorObj ->
                    when {
                        authorObj.author.name != null -> authorObj.author.name
                        true && (authorObj as Map<*, *>).containsKey("author") -> {
                            val author = (authorObj as Map<*, *>)["author"]
                            if (author is Map<*, *> && author.containsKey("name")) {
                                author["name"].toString()
                            } else null
                        }

                        else -> null
                    }
                }
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            emptyList()
        }
        val authorsFromSearch = bookResponse?.author_name ?: emptyList()
        val authors = when {
            authorsFromDetail.isNotEmpty() -> authorsFromDetail
            authorsFromSearch.isNotEmpty() -> authorsFromSearch
            else -> listOf("Unknown Author")
        }
        val coverUrl = bookResponse?.cover_i?.let {
            "https://covers.openlibrary.org/b/id/${it}-L.jpg"
        }

        val publishYear = bookResponse?.first_publish_year
        val description = when {
            input.description is String -> input.description
            true && (input.description as Map<*, *>).containsKey("value") ->
                (input.description as Map<*, *>)["value"].toString()

            else -> input.description.toString()
        }

        return Book(
            id = input.key.removePrefix("/works/"),
            title = input.title,
            authors = authors,
            coverUrl = coverUrl,
            description = description,
            publishYear = publishYear,
            isFavorite = isFavorite
        )
    }

    fun mapEntitiesToDomain(input: List<BookEntity>): List<Book> {
        return input.map {
            Book(
                id = it.bookId,
                title = it.title,
                authors = it.authors.split(","),
                coverUrl = it.coverUrl,
                description = it.description,
                publishYear = it.publishYear,
                isFavorite = it.isFavorite
            )
        }
    }

    fun mapDomainToEntity(input: Book): BookEntity {
        return BookEntity(
            bookId = input.id,
            title = input.title,
            authors = input.authors.joinToString(","),
            coverUrl = input.coverUrl,
            description = input.description,
            publishYear = input.publishYear,
            isFavorite = input.isFavorite
        )
    }
}