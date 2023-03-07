package com.start.STart.util

import android.util.Base64
import android.util.Log
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.start.STart.api.ApiClient
import com.start.STart.api.auth.response.TokenData
import java.nio.charset.StandardCharsets

// TODO: 지속적으로 리팩토링하여 고민 필요
object TokenHelper {

    const val TAG = ".TokenHelper"

    suspend fun tryLoginWithAccessToken(): Boolean {
        if(!isAccessTokenValid()) {
            Log.d("TOKEN", "tryLoginWithAccessToken: AT 미유효")
            if(!issueAccessToken()) {
                Log.d("TOKEN", "tryLoginWithAccessToken: AT 발급실패")
                return false
            }
        }

        // Api 호출
        try {
            val res = ApiClient.authService.verifyAccessToken("Bearer ${getAccessToken()}")
            if(res.isSuccessful) {
                enableToken()
            }
            return res.isSuccessful
        } catch (e: Exception) {
            return false
        }
    }

    private suspend fun enableToken() {
        ApiClient.enableToken(getAccessToken())
    }

    suspend fun clearToken() {
        putToken(Constants.KEY_ACCESS_TOKEN, "")
        putToken(Constants.KEY_REFRESH_TOKEN, "")
        ApiClient.disableToken()
    }

    private suspend fun issueAccessToken(): Boolean {
        if(!isRefreshTokenValid()) {
            Log.d("TOKEN", "tryLoginWithAccessToken: RT 미유효")
            return false
        }

        try {
            val res = ApiClient.authService.issueAccessToken(getRefreshToken())
            if(res.isSuccessful) {
                val accessToken = res.body()?.parseData(TokenData::class.java)?.accessToken
                putToken(Constants.KEY_ACCESS_TOKEN, accessToken!!)
                return true
            } else {
                return false
            }
        } catch(e: Exception) {
            Log.d(TAG, "issueAccessToken: $e")
            return false
        }
    }

    private suspend fun isAccessTokenValid(): Boolean {
        val token = getAccessToken()
        return if (token.isEmpty()) false else isValidExpiration(token)
    }

    private suspend fun isRefreshTokenValid(): Boolean {
        val token = getRefreshToken()
        return if (token.isEmpty()) false else isValidExpiration(token)
    }

    private fun isValidExpiration(token: String): Boolean {
        val payload = token.split(".")[1]
        val body = String(Base64.decode(payload, Base64.DEFAULT), StandardCharsets.UTF_8)
        return try {
            val exp = JsonParser.parseString(body).asJsonObject.get("exp").asLong * 1000
            Log.d(TAG, "isValidExpiration: $exp ${System.currentTimeMillis()}")
            exp > System.currentTimeMillis()
        } catch (e: JsonSyntaxException) {
            Log.d("TOKEN", "isValidExpiration: $e")
            false
        }
    }

    suspend fun putToken(type: String, token: String) {
        PreferenceManager.putString(type, token)
    }

    private suspend fun getAccessToken(): String {
        val token = PreferenceManager.getString(Constants.KEY_ACCESS_TOKEN)
        Log.d(TAG, "getAccessToken: $token")
        return token
    }

    private suspend fun getRefreshToken(): String {
        val token = PreferenceManager.getString(Constants.KEY_REFRESH_TOKEN)
        Log.d(TAG, "getRefreshToken: $token")
        return token
    }
}