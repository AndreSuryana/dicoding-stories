package com.andresuryana.dicodingstories.data.source.prefs

import android.content.Context
import android.content.SharedPreferences
import com.andresuryana.dicodingstories.data.model.User

private const val PREFS_NAME = "dicoding_stories_prefs"
private const val KEY_USER_ID = "KEY_USER_ID"
private const val KEY_USER_NAME = "KEY_USER_NAME"
private const val KEY_USER_EMAIL = "KEY_USER_EMAIL"
private const val KEY_USER_TOKEN = "KEY_USER_TOKEN"

class SessionHelperImpl(context: Context) : SessionHelper {

    private var prefs: SharedPreferences

    init {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    override fun isLoggedIn(): Boolean {
        return prefs.getString(KEY_USER_TOKEN, null) != null
    }

    override fun setCurrentUser(id: String, name: String, email: String, token: String) {
        prefs.edit()
            .putString(KEY_USER_ID, id)
            .putString(KEY_USER_NAME, name)
            .putString(KEY_USER_TOKEN, token)
            .apply()
    }

    override fun getCurrentUser(): User? {
        val id = prefs.getString(KEY_USER_ID, null)
        val name = prefs.getString(KEY_USER_NAME, null)
        val email = prefs.getString(KEY_USER_EMAIL, null)
        val token = prefs.getString(KEY_USER_TOKEN, null)

        return if (id != null && name != null && email != null && token != null) User(
            id,
            name,
            email,
            token
        )
        else null
    }

    override fun clearSession() {
        prefs.edit().clear().apply()
    }
}