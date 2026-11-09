package com.angelramirez.agresiembre.data

import android.content.Context
import android.content.SharedPreferences

class AuthManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("agro_db", Context.MODE_PRIVATE)

    fun registerUser(user: String, pass: String): Boolean {
        if (user.isBlank() || pass.isBlank()) return false
        prefs.edit().putString("user_$user", pass).apply()
        return true
    }

    fun loginUser(user: String, pass: String): Boolean {
        val savedPass = prefs.getString("user_$user", null)
        return savedPass != null && savedPass == pass
    }
}