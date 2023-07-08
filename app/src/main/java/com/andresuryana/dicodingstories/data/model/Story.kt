package com.andresuryana.dicodingstories.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.andresuryana.dicodingstories.data.source.local.contract.DatabaseContract.StoryEntity
import com.andresuryana.dicodingstories.data.source.local.converter.DateTypeConverter
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = StoryEntity.TABLE_NAME)
data class Story(

    @SerializedName("id")
    @PrimaryKey
    val id: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("photoUrl")
    val photoUrl: String,

    @SerializedName("createdAt")
    @TypeConverters(DateTypeConverter::class)
    var createdAt: Date,

    @SerializedName("lat")
    val latitude: Float?,

    @SerializedName("lon")
    val longitude: Float?

) : Parcelable
