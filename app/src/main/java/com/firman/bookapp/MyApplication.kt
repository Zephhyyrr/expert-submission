package com.firman.bookapp

import android.app.Application
import com.firman.bookapp.di.appModule
import com.firman.core.di.databaseModule
import com.firman.core.di.networkModule
import com.firman.core.di.repositoryModule
import com.firman.core.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(
                listOf(
                    databaseModule,
                    networkModule,
                    repositoryModule,
                    useCaseModule,
                    appModule
                )
            )
        }
    }
}