package com.andresuryana.dicodingstories.data.repository

import androidx.paging.PagingData
import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.data.model.User
import com.andresuryana.dicodingstories.util.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File

interface Repository {

    suspend fun register(name: String, email: String, password: String): Resource<Boolean>

    suspend fun login(email: String, password: String): Resource<User>

    suspend fun getStories(): Flow<PagingData<Story>>

    suspend fun addNewStory(photo: File, description: String): Resource<Boolean>
}