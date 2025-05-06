package com.firman.core.data.source.local.room

import android.content.Context
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import net.sqlcipher.database.SupportFactory
import java.security.SecureRandom

/**
 * Class untuk membuat database terenkripsi menggunakan SQLCipher
 */
object EncryptedBookDatabase {

    private const val DATABASE_NAME = "Book.db"
    private const val ENCRYPTED_PREF_NAME = "secure_db_prefs"
    private const val KEY_DATABASE_KEY = "database_encryption_key"

    /**
     * Membuat instance database Room yang terenkripsi dengan SQLCipher
     */
    fun createEncryptedDatabase(context: Context): BookDatabase {
        // Generate or retrieve a strong encryption key
        val passphrase = getOrCreateDatabaseKey(context)

        // Ubah passphrase ke bentuk bytes untuk SQLCipher
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

    /**
     * Mendapatkan atau membuat kunci enkripsi database baru
     * Kunci disimpan dalam EncryptedSharedPreferences
     */
    private fun getOrCreateDatabaseKey(context: Context): String {
        // Buat master key untuk enkripsi SharedPreferences
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        // Buat EncryptedSharedPreferences menggunakan master key
        val securePreferences = EncryptedSharedPreferences.create(
            context,
            ENCRYPTED_PREF_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        // Periksa apakah sudah ada kunci, jika tidak buat yang baru
        var key = securePreferences.getString(KEY_DATABASE_KEY, null)

        if (key == null) {
            // Generate kunci acak yang kuat
            val random = SecureRandom()
            val bytes = ByteArray(32) // 256-bit key
            random.nextBytes(bytes)

            // Konversi ke string hex untuk penyimpanan
            key = bytes.joinToString("") { "%02x".format(it) }

            // Simpan kunci secara aman
            securePreferences.edit().putString(KEY_DATABASE_KEY, key).apply()
        }

        return key
    }
}