package com.andresuryana.dicodingstories.data.source.remote

import com.andresuryana.dicodingstories.data.model.Story
import com.andresuryana.dicodingstories.data.model.User
import com.andresuryana.dicodingstories.data.model.Wrapper
import com.andresuryana.dicodingstories.util.Constants.STORIES_PAGE_SIZE
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<Wrapper<Nothing>>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<Wrapper<User>>

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = STORIES_PAGE_SIZE,
        @Query("location") location: Int = 0
    ): Response<Wrapper<List<Story>>>

    @Multipart
    @POST("stories")
    suspend fun addNewStory(
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") latitude: RequestBody? = null,
        @Part("lon") longitude: RequestBody? = null
    ): Response<Wrapper<Nothing>>
}