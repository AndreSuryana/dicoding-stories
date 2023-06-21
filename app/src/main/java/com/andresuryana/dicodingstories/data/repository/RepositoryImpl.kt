package com.andresuryana.dicodingstories.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.data.model.User
import com.andresuryana.dicodingstories.data.pagination.StoryPagingSource
import com.andresuryana.dicodingstories.data.source.prefs.SessionHelper
import com.andresuryana.dicodingstories.data.source.remote.ApiService
import com.andresuryana.dicodingstories.util.Constants.STORIES_PAGE_SIZE
import com.andresuryana.dicodingstories.util.Helper.parseErrorMessage
import com.andresuryana.dicodingstories.util.Resource
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remote: ApiService,
    private val session: SessionHelper
) : Repository {

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Resource<Boolean> {
        return try {
            val response = remote.register(name, email, password)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(true)
            } else {
                Resource.Failed(parseErrorMessage(response.errorBody()))
            }
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun login(email: String, password: String): Resource<User> {
        return try {
            val response = remote.login(email, password)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                // Store user session
                result.data.let { user ->
                    session.setCurrentUser(user.id, user.name, user.email, user.token)
                }
                Resource.Success(result.data)
            } else {
                Resource.Failed(parseErrorMessage(response.errorBody()))
            }
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }

    override suspend fun getStories(): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(pageSize = STORIES_PAGE_SIZE)
        ) {
            StoryPagingSource(remote)
        }.flow
    }

    override suspend fun addNewStory(photo: File, description: String): Resource<Boolean> {
        return try {
            val photoRequestBody = photo.asRequestBody("image/jpeg".toMediaType())
            val photoMultipart = MultipartBody.Part.createFormData(
                name = "photo",
                filename = photo.name,
                body = photoRequestBody
            )
            val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
            val response = remote.addNewStory(photoMultipart, descriptionRequestBody)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(true)
            } else {
                Resource.Failed(parseErrorMessage(response.errorBody()))
            }
        } catch (e: Exception) {
            Resource.Error(e.message)
        }
    }
}