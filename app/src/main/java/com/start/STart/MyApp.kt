package com.start.STart

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen
import com.start.STart.util.PreferenceManager

class MyApp: Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getAppContext(): Context {
            return context
        }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        // TODO: Hilt 라이브러리를 이용한 의존성 주입 (시간남으면 공부하고 적용)
        PreferenceManager.init(applicationContext)
        context = applicationContext
    }
}