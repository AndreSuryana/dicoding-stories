package com.andresuryana.dicodingstories.util

import android.content.Context

class SettingHelper(context: Context) {

    private val prefs = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE)

    fun setLanguage(language: String) {
        prefs.edit()
            .putString(SETTING_LANGUAGE, language)
            .apply()
    }

    fun setDarkMode(isDarkMode: Boolean) {
        prefs.edit()
            .putBoolean(SETTING_DARK_MODE, isDarkMode)
            .apply()
    }

    fun getLanguage(): String {
        return prefs.getString(SETTING_LANGUAGE, "en") ?: "en"
    }

    fun isDarkModeActive(): Boolean {
        return prefs.getBoolean(SETTING_DARK_MODE, false)
    }

    companion object {
        // App Setting Shared Prefs Key
        const val SETTING_PREFS_NAME = "setting_prefs"
        const val SETTING_LANGUAGE = "language"
        const val SETTING_DARK_MODE = "dark_mode"
    }
}