package com.andresuryana.dicodingstories.data.source.local.contract

object DatabaseContract {

    const val DATABASE_NAME = "story.db"

    object StoryEntity {
        const val TABLE_NAME = "stories"
    }

    object RemoteKeysEntity {
        const val TABLE_NAME = "remote_keys"
        const val COLUMN_ID = "id"
    }
}