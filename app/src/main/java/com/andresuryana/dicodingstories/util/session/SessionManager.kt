package com.andresuryana.dicodingstories.util.session

import android.content.Context
import android.content.SharedPreferences
import androidx.navigation.NavController
import com.andresuryana.dicodingstories.R
import com.andresuryana.dicodingstories.util.session.SessionManagerConstants.KEY_IS_LOGGED_IN
import com.andresuryana.dicodingstories.util.session.SessionManagerConstants.KEY_USER_ID
import com.andresuryana.dicodingstories.util.session.SessionManagerConstants.KEY_USER_NAME
import com.andresuryana.dicodingstories.util.session.SessionManagerConstants.KEY_USER_TOKEN
import com.andresuryana.dicodingstories.util.session.SessionManagerConstants.PREFS_NAME

class SessionManager(
    context: Context,
    private val navController: NavController? = null
) {

    private var prefs: SharedPreferences

    init {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun setLogin(isLoggedIn: Boolean) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    private fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    private fun setUserId(userId: String) {
        prefs.edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    private fun setUserName(userName: String) {
        prefs.edit().putString(KEY_USER_NAME, userName).apply()
    }

    fun getUserToken(): String? {
        return prefs.getString(KEY_USER_TOKEN, null)
    }

    private fun setUserToken(token: String) {
        prefs.edit().putString(KEY_USER_TOKEN, token).apply()
    }

    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    private fun clearUserAuth() {
        prefs.edit().clear().apply()
    }

    fun login(userId: String, userName: String) {
        setLogin(true)
        setUserId(userId)
        setUserName(userName)
        navController?.navigate(R.id.action_loginFragment_to_listStoryFragment)
    }

    fun logout() {
        setLogin(false)
        clearUserAuth()
        navController?.navigate(R.id.action_global_loginFragment)
    }

    fun checkLogin() {
        if (!isLoggedIn()) {
            navController?.navigate(R.id.action_global_loginFragment)
        }
    }
}