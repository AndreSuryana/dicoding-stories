package com.andresuryana.dicodingstories.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andresuryana.dicodingstories.data.source.local.contract.DatabaseContract.RemoteKeysEntity
import com.andresuryana.dicodingstories.data.source.local.entity.RemoteKeys

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKeysList(remoteKeysList: List<RemoteKeys>)

    @Query("SELECT * FROM ${RemoteKeysEntity.TABLE_NAME} WHERE ${RemoteKeysEntity.COLUMN_ID} = :id")
    suspend fun getRemoteKeysId(id: String): RemoteKeys?

    @Query("DELETE FROM ${RemoteKeysEntity.TABLE_NAME}")
    suspend fun clearRemoteKeys()
}