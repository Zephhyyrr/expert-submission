package com.firman.core.data.source.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object CertificatePinner {
    private const val HOSTNAME = "openlibrary.org"


    private val CERTIFICATE_PINS = arrayOf(
        "sha256/ol96ZIHxmCl3NPghGlBlld+i1zHH8Vxfom4SU0VWTxY=",
        "sha256/bdrBhpj38ffhxpubzkINl0rG+UyossdhcBYj+Zx2fcc=",
        "sha256/C5+lpZ7tcVwmwQIMcRtPbsQtWLABXhQzejna0wHFr8M="
    )
    fun createSecureClient(): OkHttpClient {
        val certificatePinner = okhttp3.CertificatePinner.Builder().apply {
            for (pin in CERTIFICATE_PINS) {
                add(HOSTNAME, pin)
            }
        }.build()

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .certificatePinner(certificatePinner)
            .build()
    }
}