package com.andresuryana.dicodingstories.data.source.remote.interceptor

import android.content.Context
import com.andresuryana.dicodingstories.BuildConfig
import com.andresuryana.dicodingstories.util.session.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(context: Context) : Interceptor {

    private var sessionManager: SessionManager

    init {
        sessionManager = SessionManager(context)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()

        // Set content
        request.addHeader("Accept", "application/json")

        // Set authorization
        sessionManager.getUserToken()?.let { token ->
            request.addHeader("Authorization", "Bearer $token")
        }

        return chain.proceed(request.build())
    }
}