package com.start.STart.util

import android.util.Base64
import android.util.Log
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.start.STart.api.ApiClient
import com.start.STart.api.ApiClient.enableToken
import com.start.STart.api.auth.response.TokenData
import java.nio.charset.StandardCharsets

// TODO: 에러 따로 처리해서 유저에게 알려줘야 함
object TokenHelper {

    const val TAG = ".TokenHelper"

    suspend fun tryLoginWithAccessToken(): Boolean {
        var accessToken = PreferenceManager.getString(Constants.KEY_ACCESS_TOKEN)
        Log.d(TAG, "tryLoginWithAccessToken: AT $accessToken")
        if(accessToken.isEmpty()) {
            Log.d(TAG, "tryLoginWithAccessToken: AT 공백")
            return false
        }

        val isSuccessful = issueAccessToken(accessToken) // 토큰 발급
        if(!isSuccessful) {
            Log.d(TAG, "tryLoginWithAccessToken: AT 발급 실패")
            return false
        }

        // 토큰 발급 후 업데이트
        accessToken = PreferenceManager.getString(Constants.KEY_ACCESS_TOKEN)
        return verifyToken(accessToken)
    }

    private suspend fun verifyToken(token: String): Boolean {
        try {
            val res = ApiClient.authService.verifyAccessToken("Bearer $token")
            if(res.isSuccessful) {
                Log.d(TAG, "verifyToken: 인증 성공")
                enableToken(token)
            } else {
                Log.d(TAG, "verifyToken: ${res.errorBody()?.string()}")
            }
            return res.isSuccessful
        } catch (e: Exception) {
            Log.d(TAG, "verifyToken: ${e.message}")
            return false
        }
    }

    private suspend fun issueAccessToken(accessToken: String): Boolean {
        val refreshToken = PreferenceManager.getString(Constants.KEY_REFRESH_TOKEN)
        Log.d(TAG, "issueAccessToken: RT $refreshToken")
        if(refreshToken.isEmpty()) {
            Log.d(TAG, "issueAccessToken: RT 공백")
            return false
        }

        try {
            val res = ApiClient.authService.issueAccessToken("Bearer $accessToken", "Bearer $refreshToken")
            if(res.isSuccessful) {
                val newAccessToken = res.body()?.parseData(TokenData::class.java)?.accessToken!!
                PreferenceManager.putString(Constants.KEY_ACCESS_TOKEN, newAccessToken)
                Log.d(TAG, "issueAccessToken: AT $newAccessToken")
                return true
            } else {
                Log.d(TAG, "issueAccessToken: ${res.errorBody()?.string()}")
                return false
            }
        } catch(e: Exception) {
            Log.d(TAG, "issueAccessToken: ${e.message}")
            return false
        }
    }

    // (미사용) 토큰 파싱하여 만료 기간 확인
    private fun isValidExpiration(token: String): Boolean {
        val payload = token.split(".")[1]
        val body = String(Base64.decode(payload, Base64.DEFAULT), StandardCharsets.UTF_8)
        return try {
            val exp = JsonParser.parseString(body).asJsonObject.get("exp").asLong * 1000
            exp > System.currentTimeMillis()
        } catch (e: JsonSyntaxException) {
            false
        }
    }
}