package com.febrian.sharedapps.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(c: Context) {
    private var sharedPreference: SharedPreferences =
        c.getSharedPreferences(Constant.sharedPreferences, Context.MODE_PRIVATE)

    @SuppressLint("CommitPrefEdits")
    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreference.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreference.getBoolean(key, false)
    }

    fun putInt(key: String, value: Int) {
        val editor = sharedPreference.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String): Int {
        return sharedPreference.getInt(key, 0)
    }

    fun putString(key: String, value: String) {
        val editor = sharedPreference.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String {
        return sharedPreference.getString(key, "").toString()
    }

    fun getString(key: String, default: String): String {
        return sharedPreference.getString(key, default).toString()
    }

    fun clear(key: String) {
        val editor = sharedPreference.edit()
        editor.remove(key).apply()
    }
}