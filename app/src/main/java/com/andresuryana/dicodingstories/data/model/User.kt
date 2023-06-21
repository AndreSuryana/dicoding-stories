package com.andresuryana.dicodingstories.data.model

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("userId")
    val id: String?,

    @SerializedName("name")
    val name: String,

    @SerializedName("token")
    val token: String?

)
