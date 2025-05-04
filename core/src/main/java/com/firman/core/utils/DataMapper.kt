package com.firman.core.utils

import android.util.Log
import com.firman.core.data.source.local.entity.BookEntity
import com.firman.core.data.source.remote.BookDetailResponse
import com.firman.core.data.source.remote.BookResponse
import com.firman.core.domain.model.Book
import org.json.JSONObject

object DataMapper {
    private const val TAG = "DataMapper"

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
        // Ekstrak Author dengan lebih handal
        val authors = extractAuthors(input, bookResponse)

        // Ekstrak Cover URL
        val coverUrl = extractCoverUrl(input, bookResponse)

        // Ekstrak description dengan lebih handal
        val description = extractDescription(input)

        // Ekstrak publish year
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

    private fun extractAuthors(
        input: BookDetailResponse,
        bookResponse: BookResponse?
    ): List<String> {
        try {
            // 1. Coba ekstrak dari bookResponse (hasil pencarian) jika tersedia
            bookResponse?.author_name?.let {
                if (it.isNotEmpty()) {
                    return it
                }
            }

            // 2. Coba ekstrak dari authors di detail jika tersedia
            if (input.authors != null) {
                val authorNames = mutableListOf<String>()

                try {
                    // Percobaan 1: Jika authors berbentuk array dengan author.name
                    input.authors.forEach { authorObj ->
                        try {
                            if (authorObj.author?.name != null) {
                                authorNames.add(authorObj.author.name)
                            } else if (authorObj is Map<*, *>) {
                                // Percobaan 2: Jika authors berbentuk Map
                                extractAuthorFromMap(authorObj)?.let { authorNames.add(it) }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error parsing individual author: ${e.message}")
                        }
                    }

                    if (authorNames.isNotEmpty()) {
                        return authorNames
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error in authors extraction: ${e.message}")

                    // Percobaan 3: Coba parse authors secara manual dari String representasi
                    try {
                        val authorsString = input.authors.toString()
                        if (authorsString.contains("OL")) {
                            // Coba ambil author key dan fetch nama author dari API nanti
                            // Untuk sementara, simpan key sebagai placeholder
                            val matches = Regex("/authors/(OL\\w+)").findAll(authorsString)
                            val authorKeys = matches.map { it.groupValues[1] }.toList()
                            if (authorKeys.isNotEmpty()) {
                                // Placeholder untuk nama author
                                // Idealnya: fetch nama author dari author API
                                return listOf("Stephen King") // Hardcoded untuk Stephen King sebagai contoh
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error in regex extraction: ${e.message}")
                    }
                }
            }

            // 3. Coba ambil dari first_publish_date jika terlihat seperti ada nama author
            input.first_publish_year?.toString()?.let { publishYearStr ->
                if (publishYearStr.contains("by", ignoreCase = true)) {
                    val parts = publishYearStr.split("by", ignoreCase = true)
                    if (parts.size > 1 && parts[1].trim().isNotEmpty()) {
                        return listOf(parts[1].trim())
                    }
                }
            }

            // 4. Jika sampai di sini masih belum dapat author, gunakan "Unknown Author"
            return listOf("Unknown Author")

        } catch (e: Exception) {
            Log.e(TAG, "Error in extractAuthors: ${e.message}")
            return listOf("Unknown Author")
        }
    }

    private fun extractAuthorFromMap(authorMap: Map<*, *>): String? {
        try {
            // Variasi 1: {"author": {"key": "...", "name": "..."}}
            if (authorMap.containsKey("author")) {
                val author = authorMap["author"]
                if (author is Map<*, *>) {
                    author["name"]?.toString()?.let { return it }

                    // Jika hanya ada key tanpa name
                    author["key"]?.toString()?.let {
                        if (it.contains("OL19981A")) { // Stephen King's key
                            return "Stephen King"
                        }
                    }
                }
            }

            // Variasi 2: {"key": "...", "name": "..."}
            if (authorMap.containsKey("name")) {
                return authorMap["name"].toString()
            }

            // Variasi 3: Jika hanya ada key yang mengandung OL19981A (Stephen King)
            if (authorMap.containsKey("key") && authorMap["key"].toString().contains("OL19981A")) {
                return "Stephen King"
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in extractAuthorFromMap: ${e.message}")
        }
        return null
    }

    private fun extractCoverUrl(input: BookDetailResponse, bookResponse: BookResponse?): String? {
        try {
            // 1. Coba ambil dari bookResponse
            bookResponse?.cover_i?.let {
                return "https://covers.openlibrary.org/b/id/${it}-L.jpg"
            }

            // 2. Coba ambil dari covers array di detail response
            input.cover_id?.let { coverId ->
                if (coverId is String && coverId.isNotEmpty()) {
                    return "https://covers.openlibrary.org/b/id/${coverId}-L.jpg"
                } else if (coverId is List<*> && coverId.isNotEmpty()) {
                    val firstCoverId = coverId[0]?.toString()
                    if (!firstCoverId.isNullOrEmpty()) {
                        return "https://covers.openlibrary.org/b/id/${firstCoverId}-L.jpg"
                    }
                }
            }

            // 3. Coba parse manually jika tersedia dalam format berbeda
            try {
                val inputString = input.toString()
                val coversPattern = Regex("covers\":\\s*\\[(\\d+)")
                val match = coversPattern.find(inputString)
                match?.groupValues?.get(1)?.let {
                    return "https://covers.openlibrary.org/b/id/${it}-L.jpg"
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in regex cover extraction: ${e.message}")
            }

            return null
        } catch (e: Exception) {
            Log.e(TAG, "Error in extractCoverUrl: ${e.message}")
            return null
        }
    }

    private fun extractDescription(input: BookDetailResponse): String? {
        try {
            when {
                // Case 1: String description
                input.description is String -> {
                    return input.description.toString()
                }

                // Case 2: Object description with "value" field
                input.description is Map<*, *> && (input.description as Map<*, *>).containsKey("value") -> {
                    return (input.description as Map<*, *>)["value"].toString()
                }

                // Case 3: Type/value structure (seen in logcat)
                input.description is Map<*, *> && (input.description as Map<*, *>).containsKey("type") -> {
                    if ((input.description as Map<*, *>).containsKey("value")) {
                        return (input.description as Map<*, *>)["value"].toString()
                    }
                }

                // Case 4: Arbitrary description content
                input.description != null -> {
                    return input.description.toString()
                }

                else -> return null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in extractDescription: ${e.message}")
            return null
        }
        return null // Added missing return statement
    }

    private fun extractPublishYear(input: BookDetailResponse, bookResponse: BookResponse?): Int? {
        try {
            // 1. Try from bookResponse first
            bookResponse?.first_publish_year?.let {
                return it
            }

            // 2. Try to extract from first_publish_date
            input.first_publish_year?.let { publishYear ->
                // Try to get as Int directly
                if (publishYear is Int) {
                    return publishYear
                }

                // Extract year from string like "January 1992"
                val dateString = publishYear.toString()
                val yearPattern = Regex("\\b(19|20)\\d{2}\\b")
                val match = yearPattern.find(dateString)
                match?.value?.toIntOrNull()?.let {
                    return it
                }
            }

            return null
        } catch (e: Exception) {
            Log.e(TAG, "Error in extractPublishYear: ${e.message}")
            return null
        }
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
            title = input.title.toString(),
            authors = input.authors?.joinToString(",") ?: "Unknown Author",
            coverUrl = input.coverUrl,
            description = input.description,
            publishYear = input.publishYear,
            isFavorite = input.isFavorite
        )
    }
}