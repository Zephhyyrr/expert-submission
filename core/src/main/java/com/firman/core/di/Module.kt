package com.firman.core.di

import com.firman.core.data.source.local.LocalDataSource
import com.firman.core.data.source.local.room.EncryptedBookDatabase
import com.firman.core.data.source.remote.ApiService
import com.firman.core.data.source.remote.CertificatePinner
import com.firman.core.data.source.remote.RemoteDataSource
import com.firman.core.domain.repository.BookRepository
import com.firman.core.domain.repository.BookRepositoryImpl
import com.firman.core.domain.usecase.BookInteractor
import com.firman.core.domain.usecase.BookUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val databaseModule = module {
    factory { get<com.firman.core.data.source.local.room.BookDatabase>().bookDao() }
    single {
        EncryptedBookDatabase.createEncryptedDatabase(androidContext())
    }
}

val networkModule = module {
    single {
        CertificatePinner.createSecureClient()
    }
    single {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://openlibrary.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    single<BookRepository> { BookRepositoryImpl(get(), get()) }
}

val useCaseModule = module {
    factory<BookUseCase> { BookInteractor(get()) }
}