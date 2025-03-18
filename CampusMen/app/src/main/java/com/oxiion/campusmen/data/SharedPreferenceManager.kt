package com.oxiion.campusmen.data

import android.content.Context

object SharedPreferencesManager {
    private const val PREF_NAME = "AppPreferences"
    private const val KEY_EMAIL = "email"
    private const val KEY_VERIFICATION_CODE = "verificationCode"
    private const val KEY_IS_LOGGED_IN = "isLoggedIn"

    // Save Email
    fun saveEmail(context: Context, email: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(KEY_EMAIL, email).apply()
    }

    // Get Email
    fun getEmail(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_EMAIL, null)
    }

    // Save Verification Code
    fun saveVerificationCode(context: Context, code: String) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(KEY_VERIFICATION_CODE, code).apply()
    }

    // Get Verification Code
    fun getVerificationCode(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_VERIFICATION_CODE, null)
    }

    // Save Login State
    fun setLoggedIn(context: Context, isLoggedIn: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    // Check if the user is logged in
    fun isLoggedIn(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
}
