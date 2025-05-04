package com.firman.core.domain.repository

import android.util.Log
import com.firman.core.data.Resource
import com.firman.core.data.source.local.LocalDataSource
import com.firman.core.data.source.remote.ApiResponse
import com.firman.core.data.source.remote.RemoteDataSource
import com.firman.core.domain.model.Book
import com.firman.core.utils.DataMapper
import kotlinx.coroutines.flow.*

class BookRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : BookRepositoryImpl {


    override fun searchBooks(query: String): Flow<Resource<List<Book>>> {
        return flow {
            emit(Resource.Loading())
            try {
                when (val apiResponse = remoteDataSource.searchBooks(query).first()) {
                    is ApiResponse.Success -> {
                        val bookList = DataMapper.mapResponsesToDomain(apiResponse.data)
                        emit(Resource.Success(bookList))
                    }

                    is ApiResponse.Empty -> {
                        emit(Resource.Success(emptyList()))
                    }

                    is ApiResponse.Error -> {
                        emit(Resource.Error<List<Book>>(apiResponse.errorMessage))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error in searchBooks: ${e.message}")
                emit(Resource.Error<List<Book>>("Failed to search books: ${e.message}"))
            }
        }
    }

    override fun getBookDetail(bookId: String): Flow<Resource<Book>> {
        return flow {
            emit(Resource.Loading())
            try {
                // 1. Get book detail
                Log.d(TAG, "Fetching detail for book ID: $bookId")
                val detailResponse = remoteDataSource.getBookDetail(bookId).first()

                when (detailResponse) {
                    is ApiResponse.Success -> {
                        // 2. Check if book is favorite
                        val isFavorite = localDataSource.isFavorite(bookId).first()
                        Log.d(TAG, "Book is favorite: $isFavorite")

                        // 3. Get additional book information from search API if available
                        var bookResponse = try {
                            val title = detailResponse.data.title
                            Log.d(TAG, "Searching for additional info with title: $title")
                            val searchResponse = remoteDataSource.searchBooks(title).first()

                            when (searchResponse) {
                                is ApiResponse.Success -> {
                                    // Try to find exact match by ID
                                    val exactMatch = searchResponse.data.find {
                                        it.key?.contains(bookId, ignoreCase = true) == true
                                    }

                                    if (exactMatch != null) {
                                        Log.d(TAG, "Found exact match in search results")
                                        exactMatch
                                    } else if (searchResponse.data.isNotEmpty()) {
                                        Log.d(TAG, "Using first search result as no exact match found")
                                        searchResponse.data.first()
                                    } else {
                                        Log.d(TAG, "No search results found")
                                        null
                                    }
                                }
                                else -> {
                                    Log.d(TAG, "Search API did not return results")
                                    null
                                }
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error getting additional book data: ${e.message}")
                            null
                        }

                        try {
                            Log.d(TAG, "Mapping response to domain model")
                            val book = DataMapper.mapResponseToDomain(
                                detailResponse.data,
                                bookResponse,
                                isFavorite
                            )

                            Log.d(TAG, "Mapped book: id=${book.id}, title=${book.title}")
                            Log.d(TAG, "Authors: ${book.authors?.joinToString() ?: "null"}")
                            Log.d(TAG, "Publish Year: ${book.publishYear}")
                            Log.d(TAG, "Cover URL: ${book.coverUrl}")

                            emit(Resource.Success(book))
                        } catch (e: Exception) {
                            Log.e(TAG, "Error mapping book data: ${e.message}")
                            emit(Resource.Error<Book>("Error processing book data: ${e.message}"))
                        }
                    }

                    is ApiResponse.Empty -> {
                        Log.d(TAG, "Book detail API returned empty response")
                        emit(Resource.Error<Book>("Book not found"))
                    }

                    is ApiResponse.Error -> {
                        Log.e(TAG, "Book detail API error: ${detailResponse.errorMessage}")
                        emit(Resource.Error<Book>(detailResponse.errorMessage))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Unexpected error in getBookDetail: ${e.message}")
                emit(Resource.Error<Book>(e.message ?: "Unknown error occurred"))
            }
        }
    }

    override fun getFavoriteBooks(): Flow<List<Book>> {
        return localDataSource.getFavoriteBooks().map {
            DataMapper.mapEntitiesToDomain(it)
        }
    }

    override suspend fun setFavoriteBook(book: Book, state: Boolean) {
        val bookEntity = DataMapper.mapDomainToEntity(book.copy(isFavorite = state))
        localDataSource.insertBook(bookEntity)
    }

    override fun isFavorite(bookId: String): Flow<Boolean> {
        return localDataSource.isFavorite(bookId)
    }

    companion object {
        private val TAG = "BookRepository"
    }
}

