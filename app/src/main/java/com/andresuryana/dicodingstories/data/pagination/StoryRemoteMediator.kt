package com.andresuryana.dicodingstories.data.pagination

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.data.source.local.StoryDatabase
import com.andresuryana.dicodingstories.data.source.local.entity.RemoteKeys
import com.andresuryana.dicodingstories.data.source.remote.ApiService

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val remote: ApiService,
    private val local: StoryDatabase
) : RemoteMediator<Int, Story>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Story>): MediatorResult {
        return try {
            // Get page position according to loadType
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_POSITION
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                        endOfPaginationReached = remoteKeys != null
                    )
                    nextKey
                }
            }

            // Request data to remote service
            val response = remote.getStories(page, state.config.pageSize)
            val result = response.body()
            val endOfPageReached = result?.data?.isEmpty() == true

            // Handle data result
            local.withTransaction {
                // Clear database on REFRESH
                if (loadType == LoadType.REFRESH) {
                    local.storyDao().clearStories()
                    local.remoteKeysDao().clearRemoteKeys()
                }

                // Manage remote keys
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPageReached) null else page + 1
                val keys = result?.data?.map {
                    RemoteKeys(it.id, prevKey, nextKey)
                }

                // Insert into database
                result?.data?.let {
                    local.storyDao().insertStories(it)
                }
                keys?.let {
                    local.remoteKeysDao().insertRemoteKeysList(it)
                }
            }
            MediatorResult.Success(
                endOfPaginationReached = endOfPageReached
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Story>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            local.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Story>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            local.remoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Story>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                local.remoteKeysDao().getRemoteKeysId(id)
            }
        }
    }

    companion object {
        const val INITIAL_PAGE_POSITION = 1
    }
}