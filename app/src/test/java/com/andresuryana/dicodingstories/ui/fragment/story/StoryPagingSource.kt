package com.andresuryana.dicodingstories.ui.fragment.story

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andresuryana.dicodingstories.data.model.Story

class StoryPagingSource : PagingSource<Int, LiveData<Story>>() {

    override fun getRefreshKey(state: PagingState<Int, LiveData<Story>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<Story>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

    companion object {
        fun snapshot(stories: List<Story>): PagingData<Story> {
            return PagingData.from(stories)
        }
    }
}