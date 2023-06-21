package com.andresuryana.dicodingstories.data.source.prefs

import com.andresuryana.dicodingstories.data.model.User

interface SessionHelper {

    fun isLoggedIn(): Boolean

    fun setCurrentUser(id: String, name: String, email: String, token: String)

    fun getCurrentUser(): User?

    fun clearSession()
}