package com.example.practiceapi.prefs

import android.content.Context
import android.util.Log

class Prefs(context: Context) {
    private val prefNm = "mPref"
    private val prefs = context.getSharedPreferences(prefNm, Context.MODE_PRIVATE)
    val editor =prefs.edit()

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, defValue: String) {
        prefs.edit().putString(key, defValue).apply()
        Log.d("shin", "token : ${getString(key, defValue).toString()}")
    }

}
