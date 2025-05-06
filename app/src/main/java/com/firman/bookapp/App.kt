package com.firman.bookapp

import com.firman.bookapp.di.appModule
import com.firman.core.di.databaseModule
import com.firman.core.di.networkModule
import com.firman.core.di.repositoryModule
import com.firman.core.di.useCaseModule
import com.google.android.play.core.splitcompat.SplitCompatApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : SplitCompatApplication() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
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