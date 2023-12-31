package com.andresuryana.dicodingstories.di

import android.content.Context
import androidx.room.Room
import com.andresuryana.dicodingstories.BuildConfig
import com.andresuryana.dicodingstories.data.repository.Repository
import com.andresuryana.dicodingstories.data.repository.RepositoryImpl
import com.andresuryana.dicodingstories.data.source.local.StoryDatabase
import com.andresuryana.dicodingstories.data.source.local.contract.DatabaseContract
import com.andresuryana.dicodingstories.data.source.prefs.SessionHelper
import com.andresuryana.dicodingstories.data.source.prefs.SessionHelperImpl
import com.andresuryana.dicodingstories.data.source.remote.ApiService
import com.andresuryana.dicodingstories.data.source.remote.interceptor.ErrorInterceptor
import com.andresuryana.dicodingstories.data.source.remote.interceptor.HeaderInterceptor
import com.andresuryana.dicodingstories.data.source.remote.interceptor.NetworkConnectivityInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSessionHelper(@ApplicationContext context: Context): SessionHelper =
        SessionHelperImpl(context)

    @Provides
    @Singleton
    fun provideRepository(
        remote: ApiService,
        local: StoryDatabase,
        session: SessionHelper
    ): Repository = RepositoryImpl(remote, local, session)

    @Provides
    @Singleton
    fun provideApiService(@ApplicationContext context: Context): ApiService {
        // Interceptors
        val connectivityInterceptor = NetworkConnectivityInterceptor(context)
        val headerInterceptor = HeaderInterceptor(context)
        val errorInterceptor = ErrorInterceptor()
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        // Client
        val client = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(connectivityInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor(errorInterceptor)
            .addInterceptor(loggingInterceptor)
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

    @Singleton
    @Provides
    fun provideStoryDatabase(@ApplicationContext context: Context): StoryDatabase {
        return Room.databaseBuilder(
            context,
            StoryDatabase::class.java,
            DatabaseContract.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
}