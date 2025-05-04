package com.firman.core.domain.repository

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
                        emit(Resource.Error(apiResponse.errorMessage))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error("Failed to search books: ${e.message}"))
            }
        }
    }

    override fun getBookDetail(bookId: String): Flow<Resource<Book>> {
        return flow {
            emit(Resource.Loading())
            try {
                val detailResponse = remoteDataSource.getBookDetail(bookId).first()
                when (detailResponse) {
                    is ApiResponse.Success -> {
                        val isFavorite = localDataSource.isFavorite(bookId).first()

                        val bookResponse = try {
                            val title = detailResponse.data.title
                            val searchResponse = remoteDataSource.searchBooks(title).first()
                            when (searchResponse) {
                                is ApiResponse.Success -> {
                                    searchResponse.data.find {
                                        it.key.contains(bookId, ignoreCase = true)
                                    } ?: searchResponse.data.firstOrNull()
                                }
                                else -> null
                            }
                        } catch (_: Exception) {
                            null
                        }

                        try {
                            val book = DataMapper.mapResponseToDomain(
                                detailResponse.data,
                                bookResponse,
                                isFavorite
                            )
                            emit(Resource.Success(book))
                        } catch (e: Exception) {
                            emit(Resource.Error("Error processing book data: ${e.message}"))
                        }
                    }

                    is ApiResponse.Empty -> {
                        emit(Resource.Error("Book not found"))
                    }

                    is ApiResponse.Error -> {
                        emit(Resource.Error(detailResponse.errorMessage))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error(e.message ?: "Unknown error occurred"))
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
}
