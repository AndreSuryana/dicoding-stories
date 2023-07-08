package com.andresuryana.dicodingstories.util

import com.andresuryana.dicodingstories.data.model.Story
import java.time.Instant
import java.util.Date

object DummyData {

    fun generateDummyStories(): List<Story> {
        val stories: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            stories.add(
                Story(
                    id = i.toString(),
                    "Person $i",
                    "Post description $i",
                    "url/photo/$i",
                    Date.from(Instant.now()),
                    null,
                    null
                )
            )
        }
        return stories
    }
}