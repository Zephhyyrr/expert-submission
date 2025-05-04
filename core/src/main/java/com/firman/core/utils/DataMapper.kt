package com.firman.core.utils

import android.util.Log
import com.firman.core.data.source.local.entity.BookEntity
import com.firman.core.data.source.remote.BookDetailResponse
import com.firman.core.data.source.remote.BookResponse
import com.firman.core.domain.model.Book

object DataMapper {
    private const val TAG = "DataMapper"

    fun mapResponsesToDomain(input: List<BookResponse>): List<Book> {
        return input.map {
            Book(
                id = it.key.removePrefix("/works/"),
                title = it.title,
                authors = (it.author_name?.joinToString(", ") ?: "Unknown Author"),
                coverUrl = it.cover_i?.let { id -> "https://covers.openlibrary.org/b/id/${id}-L.jpg" },
                description = null,
                publishYear = it.first_publish_year,
                isFavorite = false
            )
        }
    }

    fun mapEntitiesToDomain(input: List<BookEntity>): List<Book> {
        return input.map {
            Book(
                id = it.bookId,
                title = it.title,
                authors = it.authors,
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
            title = input.title.toString(),
            authors = input.authors,
            coverUrl = input.coverUrl,
            description = input.description,
            publishYear = input.publishYear,
            isFavorite = input.isFavorite
        )
    }

    fun mapResponseToDomain(
        input: BookDetailResponse,
        bookResponse: BookResponse?,
        isFavorite: Boolean = false
    ): Book {
        val authors = extractAuthors(input, bookResponse)
        val coverUrl = extractCoverUrl(input, bookResponse)
        val description = extractDescription(input)
        val publishYear = extractPublishYear(input, bookResponse)

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

    private fun extractAuthors(input: BookDetailResponse, bookResponse: BookResponse?): String {
        return try {
            bookResponse?.author_name?.takeIf { it.isNotEmpty() }?.joinToString(", ")?.let {
                return it
            }

            val authorNames = mutableListOf<String>()
            input.authors?.forEach { authorObj ->
                try {
                    if (authorObj.author?.name != null) {
                        authorNames.add(authorObj.author.name)
                    } else if (authorObj is Map<*, *>) {
                        extractAuthorFromMap(authorObj)?.let { authorNames.add(it) }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing author: ${e.message}")
                }
            }
            if (authorNames.isNotEmpty()) return authorNames.joinToString(", ")

            "Unknown Author"
        } catch (e: Exception) {
            Log.e(TAG, "Error in extractAuthors: ${e.message}")
            "Unknown Author"
        }
    }

    private fun extractAuthorFromMap(authorMap: Map<*, *>): String? {
        return try {
            if (authorMap.containsKey("author")) {
                val author = authorMap["author"]
                if (author is Map<*, *>) {
                    author["name"]?.toString() ?: author["key"]?.toString()
                } else null
            } else {
                authorMap["name"]?.toString() ?: authorMap["key"]?.toString()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in extractAuthorFromMap: ${e.message}")
            null
        }
    }

    private fun extractCoverUrl(input: BookDetailResponse, bookResponse: BookResponse?): String? {
        return try {
            bookResponse?.cover_i?.let {
                return "https://covers.openlibrary.org/b/id/${it}-L.jpg"
            }

            input.cover_id?.let { coverId ->
                if (coverId is String) {
                    return "https://covers.openlibrary.org/b/id/${coverId}-L.jpg"
                } else if (coverId is List<*>) {
                    coverId.firstOrNull()?.toString()?.let {
                        return "https://covers.openlibrary.org/b/id/${it}-L.jpg"
                    }
                }
            }

            val inputString = input.toString()
            val coversPattern = Regex("covers\":\\s*\\[(\\d+)")
            val match = coversPattern.find(inputString)
            match?.groupValues?.get(1)?.let {
                "https://covers.openlibrary.org/b/id/${it}-L.jpg"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in extractCoverUrl: ${e.message}")
            null
        }
    }

    private fun extractDescription(input: BookDetailResponse): String? {
        return try {
            when (val desc = input.description) {
                is String -> desc
                is Map<*, *> -> desc["value"]?.toString()
                else -> null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in extractDescription: ${e.message}")
            null
        }
    }

    private fun extractPublishYear(input: BookDetailResponse, bookResponse: BookResponse?): Int? {
        return (input.first_publish_year ?: bookResponse?.first_publish_year) as Int?
    }
}
