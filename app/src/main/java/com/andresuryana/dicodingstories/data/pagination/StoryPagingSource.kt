package com.andresuryana.dicodingstories.data.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.data.model.Wrapper
import com.andresuryana.dicodingstories.data.source.remote.ApiService
import com.google.gson.Gson

class StoryPagingSource(private val remote: ApiService) : PagingSource<Int, Story>() {

    override fun getRefreshKey(state: PagingState<Int, Story>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
        return try {
            val page = params.key ?: 1
            val response = remote.getStories(page)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                LoadResult.Page(
                    data = result.data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (result.data.isEmpty()) null else page + 1
                )
            } else {
                val errorBody = response.errorBody().toString()
                val errorResponse = Gson().fromJson(errorBody, Wrapper::class.java)
                LoadResult.Error(Throwable(errorResponse.message))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}