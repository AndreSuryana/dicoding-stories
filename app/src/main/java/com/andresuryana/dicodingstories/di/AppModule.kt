package com.andresuryana.dicodingstories.di

import android.content.Context
import com.andresuryana.dicodingstories.BuildConfig
import com.andresuryana.dicodingstories.data.repository.Repository
import com.andresuryana.dicodingstories.data.repository.RepositoryImpl
import com.andresuryana.dicodingstories.data.source.remote.ApiService
import com.andresuryana.dicodingstories.data.source.remote.interceptor.ErrorInterceptor
import com.andresuryana.dicodingstories.data.source.remote.interceptor.HeaderInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRepository(remote: ApiService): Repository = RepositoryImpl(remote)

    @Provides
    @Singleton
    fun provideApiService(@ApplicationContext context: Context): ApiService {
        // Interceptors
        val headerInterceptor = HeaderInterceptor(context)
        val errorInterceptor = ErrorInterceptor()

        // Client
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(headerInterceptor)
            .addInterceptor(errorInterceptor)
            .build()

        // Converter Factory
        val gsonConverterFactory = GsonConverterFactory.create(
            Gson().newBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create()
        )
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(ApiService::class.java)
    }
}