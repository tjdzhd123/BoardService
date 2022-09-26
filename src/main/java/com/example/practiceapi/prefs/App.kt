package com.example.practiceapi.prefs

import android.app.Application

class App: Application() {
    companion object {
        lateinit var prefs: Prefs
    }
    override fun onCreate() {
        prefs = Prefs(applicationContext)
        //토큰 저장하기
//        App.prefs.token = token
        //토큰 가져오기
//        val token = App.prefs.token

        super.onCreate()
    }
}