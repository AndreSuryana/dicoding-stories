package com.andresuryana.dicodingstories.util

import com.andresuryana.dicodingstories.data.model.Wrapper
import com.google.gson.Gson
import okhttp3.ResponseBody

object Helper {

    fun parseErrorMessage(errorBody: ResponseBody?): String {
        val errorBodyString = errorBody?.string()
        return Gson().fromJson(errorBodyString, Wrapper::class.java).message
    }
}