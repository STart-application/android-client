package com.start.STart.util

import android.util.Log
import com.start.STart.api.ApiClient
import com.start.STart.api.ApiError
import com.start.STart.api.auth.response.TokenData

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

        return verifyToken(accessToken)
    }

    private suspend fun verifyToken(accessToken: String): Boolean {
        try {
            val res = ApiClient.authService.verifyAccessToken("Bearer $accessToken")
            if(res.isSuccessful) {
                Log.d(TAG, "verifyToken: 토큰 인증 성공")
                ApiClient.enableToken()
                return true
            } else {
                Log.d(TAG, "verifyToken: 토큰 인증 실패")
                val errorBodyString = res.errorBody()?.string()
                if(ApiClient.parseBody(errorBodyString).errorCode == ApiError.ST011.name) {
                    Log.d(TAG, "verifyToken: 토큰 재발급 시작")
                    return issueAccessToken(accessToken) // 토큰 발급
                }
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d(TAG, "verifyToken: 토큰 인증 실패 ${e.message}")
            return false
        }
    }

    suspend fun issueAccessToken(accessToken: String): Boolean {
        val refreshToken = PreferenceManager.getString(Constants.KEY_REFRESH_TOKEN)
        Log.d(TAG, "issueAccessToken: 리프레시 토큰 확인 RT $refreshToken")
        if(refreshToken.isEmpty()) {
            Log.d(TAG, "issueAccessToken: RT 공백")
            return false
        }

        try {
            val res = ApiClient.authService.issueAccessToken("Bearer $accessToken", "Bearer $refreshToken")
            if(res.isSuccessful) {
                val newAccessToken = res.body()?.parseData(TokenData::class.java)?.accessToken!!
                PreferenceManager.putString(Constants.KEY_ACCESS_TOKEN, newAccessToken)
                Log.d(TAG, "issueAccessToken: 토큰 재발급 성공 AT $newAccessToken")
                return verifyToken(newAccessToken)
            } else {
                Log.d(TAG, "issueAccessToken: 토큰 재발급 실패 ${res.errorBody()?.string()}")
                return false
            }
        } catch(e: Exception) {
            Log.d(TAG, "issueAccessToken: 토큰 재발급 에러 ${e.message}")
            return false
        }
    }
}