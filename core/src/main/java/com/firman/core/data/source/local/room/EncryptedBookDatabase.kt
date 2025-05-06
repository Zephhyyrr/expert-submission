package com.firman.core.data.source.local.room

import android.content.Context
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import net.sqlcipher.database.SupportFactory
import java.security.SecureRandom

object EncryptedBookDatabase {

    private const val DATABASE_NAME = "Book.db"
    private const val ENCRYPTED_PREF_NAME = "secure_db_prefs"
    private const val KEY_DATABASE_KEY = "database_encryption_key"

    fun createEncryptedDatabase(context: Context): BookDatabase {
        val passphrase = getOrCreateDatabaseKey(context)

        val passphraseBytes = passphrase.toByteArray()
        val factory = SupportFactory(passphraseBytes)

        return Room.databaseBuilder(
            context,
            BookDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .openHelperFactory(factory)
            .build()
    }


    private fun getOrCreateDatabaseKey(context: Context): String {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val securePreferences = EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        var key = securePreferences.getString(KEY_DATABASE_KEY, null)

        if (key == null) {
            val random = SecureRandom()
            val bytes = ByteArray(32)
            random.nextBytes(bytes)

            key = bytes.joinToString("") { "%02x".format(it) }

            securePreferences.edit().putString(KEY_DATABASE_KEY, key).apply()
        }

        return key
    }
}