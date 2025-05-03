package com.firman.core.data.source.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search.json")
    suspend fun searchBooks(@Query("q") query: String): SearchResponse

    @GET("works/{id}.json")
    suspend fun getBookDetail(@Path("id") id: String): BookDetailResponse
}