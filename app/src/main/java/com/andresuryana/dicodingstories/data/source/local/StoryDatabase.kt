package com.andresuryana.dicodingstories.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.data.source.local.converter.DateTypeConverter
import com.andresuryana.dicodingstories.data.source.local.dao.RemoteKeysDao
import com.andresuryana.dicodingstories.data.source.local.dao.StoryDao
import com.andresuryana.dicodingstories.data.source.local.entity.RemoteKeys

@TypeConverters(DateTypeConverter::class)
@Database(entities = [Story::class, RemoteKeys::class], version = 2, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao

    abstract fun remoteKeysDao(): RemoteKeysDao
}