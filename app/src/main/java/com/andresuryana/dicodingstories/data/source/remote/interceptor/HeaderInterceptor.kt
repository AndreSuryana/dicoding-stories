package com.andresuryana.dicodingstories.data.source.remote.interceptor

import android.content.Context
import com.andresuryana.dicodingstories.data.source.prefs.SessionHelper
import com.andresuryana.dicodingstories.data.source.prefs.SessionHelperImpl
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(context: Context) : Interceptor {

    private var sessionManager: SessionHelper

    init {
        sessionManager = SessionHelperImpl(context)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        // Set content
        request.addHeader("Accept", "application/json")

        // Set authorization
        sessionManager.getCurrentUser()?.let { user ->
            request.addHeader("Authorization", "Bearer ${user.token}")
        }

        return chain.proceed(request.build())
    }
}