package com.start.STart.util

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    suspend fun <T> saveToPreferences(
        key: String,
        dataClass: T,
    ) {
        val gson = Gson()
        val json = gson.toJson(dataClass)

        withContext(Dispatchers.IO) {
            putString(key, json)
        }
    }

    inline fun <reified T> loadFromPreferences(
        key: String,
    ): T? {
        val gson = Gson()
        val json = getString(key)

        return if (json.isNotBlank()) {
                gson.fromJson(json, T::class.java)
            } else {
                null
            }

    }

    fun putString(key: String, value: String) {
        pref.edit()
            .putString(key, value)
            .apply()
    }

    fun getString(key: String): String {
        return pref.getString(key, "") ?: ""
    }

    fun putBoolean(key: String, value: Boolean) {
        pref.edit()
            .putBoolean(key, value)
            .apply()
    }

    fun remove(key: String) {
        pref.edit()
            .remove(key)
            .apply()
    }

    fun getBoolean(key: String): Boolean {
        return pref.getBoolean(key, false)
    }

    fun clear() {
        pref.edit()
            .clear()
            .apply()
    }
}