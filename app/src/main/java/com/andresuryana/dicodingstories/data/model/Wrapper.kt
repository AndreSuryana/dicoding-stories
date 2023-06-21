package com.andresuryana.dicodingstories.data.model

import com.google.gson.annotations.SerializedName

data class Wrapper<out T>(

    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

    @SerializedName("loginResult", alternate = ["listStory", "story"])
    val data: T
)
