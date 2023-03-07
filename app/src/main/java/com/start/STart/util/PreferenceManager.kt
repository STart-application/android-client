package com.start.STart.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object PreferenceManager {

    private lateinit var pref: SharedPreferences

    fun init(context: Context) {
        val masterKey = MasterKey.Builder(context) // Default MasterKeyAlias 사용
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        pref = EncryptedSharedPreferences.create(
            context,
            Constants.KEY_ENCRYPTED_PREFERENCE,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    suspend fun putString(key: String, value: String) {
        pref.edit()
            .putString(key, value)
            .apply()
    }

    suspend fun getString(key: String): String {
        return pref.getString(key, "") ?: ""
    }

}