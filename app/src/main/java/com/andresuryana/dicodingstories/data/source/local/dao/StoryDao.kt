package com.andresuryana.dicodingstories.data.source.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.data.source.local.contract.DatabaseContract.StoryEntity

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<Story>)

    @Query("SELECT * FROM ${StoryEntity.TABLE_NAME}")
    fun getStories(): PagingSource<Int, Story>

    @Query("DELETE FROM ${StoryEntity.TABLE_NAME}")
    suspend fun clearStories()
}