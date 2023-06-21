package com.andresuryana.dicodingstories.data.source.remote

import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.data.model.User
import com.andresuryana.dicodingstories.data.model.Wrapper
import com.andresuryana.dicodingstories.util.Constants.STORIES_PAGE_SIZE
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("/register")
    fun register(name: String, email: String, password: String): Response<Wrapper<Nothing>>

    @POST("/login")
    fun login(email: String, password: String): Response<Wrapper<User>>

    @GET("/stories")
    fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = STORIES_PAGE_SIZE,
        @Query("location") location: Int = 0
    ): Response<Wrapper<List<Story>>>

    @Multipart
    @POST("/stories")
    fun addNewStory(
        @Part("photo") photo: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Response<Wrapper<Nothing>>
}