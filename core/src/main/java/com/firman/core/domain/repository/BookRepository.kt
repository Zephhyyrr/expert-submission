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
        }
    }


    override fun getBookDetail(bookId: String): Flow<Resource<Book>> {
        return flow {
            emit(Resource.Loading())
            try {
                when (val detailResponse = remoteDataSource.getBookDetail(bookId).first()) {
                    is ApiResponse.Success -> {
                        val isFavorite = localDataSource.isFavorite(bookId).first()
                        val searchResponse =
                            remoteDataSource.searchBooks(detailResponse.data.title).first()
                        val bookResponse = when (searchResponse) {
                            is ApiResponse.Success -> {
                                searchResponse.data.find {
                                    it.key?.contains(
                                        bookId,
                                        ignoreCase = true
                                    ) == true
                                }
                                    ?: searchResponse.data.firstOrNull()
                            }

                            else -> null
                        }
                        val book = DataMapper.mapResponseToDomain(
                            detailResponse.data,
                            bookResponse,
                            isFavorite
                        )
                        emit(Resource.Success(book))
                    }

                    is ApiResponse.Empty -> {
                        emit(Resource.Error<Book>("Book not found"))
                    }

                    is ApiResponse.Error -> {
                        emit(Resource.Error<Book>(detailResponse.errorMessage))
                    }
                }
            } catch (e: Exception) {
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
}