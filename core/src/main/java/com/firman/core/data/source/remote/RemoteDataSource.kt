package com.firman.core.data.source.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

class RemoteDataSource(private val apiService: ApiService) {
    fun searchBooks(query: String): Flow<ApiResponse<List<BookResponse>>> {
        return flow {
            try {
                val response = apiService.searchBooks(query)
                val books = response.docs ?: emptyList()
                if (books.isNotEmpty()) {
                    emit(ApiResponse.Success(books))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getBookDetail(bookId: String): Flow<ApiResponse<BookDetailResponse>> {
        return flow {
            try {
                val response = apiService.getBookDetail(bookId)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}
