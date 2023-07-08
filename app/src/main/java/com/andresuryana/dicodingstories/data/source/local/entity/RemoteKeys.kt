package com.andresuryana.dicodingstories.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andresuryana.dicodingstories.data.source.local.contract.DatabaseContract.RemoteKeysEntity

@Entity(tableName = RemoteKeysEntity.TABLE_NAME)
data class RemoteKeys(

    @PrimaryKey
    val id: String,

    val prevKey: Int?,

    val nextKey: Int?
)