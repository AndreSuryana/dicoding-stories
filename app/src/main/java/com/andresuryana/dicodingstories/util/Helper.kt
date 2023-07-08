package com.andresuryana.dicodingstories.util

import com.andresuryana.dicodingstories.data.model.Wrapper
import com.google.gson.Gson
import okhttp3.ResponseBody

object Helper {

    private const val MAX_DESCRIPTION_LENGTH = 15

    fun parseErrorMessage(errorBody: ResponseBody?): String {
        val errorBodyString = errorBody?.string()
        return Gson().fromJson(errorBodyString, Wrapper::class.java).message
    }

    fun formatStoryDescriptionForMaps(description: String): String {
        return if (description.length > MAX_DESCRIPTION_LENGTH)
            description.substring(0, MAX_DESCRIPTION_LENGTH - 1).plus("...")
        else description
    }
}