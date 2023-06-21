package com.andresuryana.dicodingstories.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Story(

    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("photoUrl")
    val photoUrl: String,

    @SerializedName("createdAt")
    val createdAt: Date,

    @SerializedName("lat")
    val latitude: Float?,

    @SerializedName("lon")
    val longitude: Float?

)
