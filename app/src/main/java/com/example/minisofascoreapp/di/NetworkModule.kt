package com.example.minisofascoreapp.di

import com.example.minisofascoreapp.data.remote.api.SofascoreApi
import com.example.minisofascoreapp.data.repository.EventRepositoryImpl
import com.example.minisofascoreapp.data.repository.TournamentRepositoryImpl
import com.example.minisofascoreapp.domain.repository.EventRepository
import com.example.minisofascoreapp.domain.repository.TournamentRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideSofascoreApi(okHttpClient: OkHttpClient): SofascoreApi {
        val json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }

        val contentType = "application/json".toMediaType()

        return Retrofit.Builder()
            .baseUrl("https://academy-backend.sofascore.dev/")
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(okHttpClient)
            .build()
            .create(SofascoreApi::class.java)
    }

    @Provides
    @Singleton
    fun provideEventRepository(api: SofascoreApi): EventRepository {
        return EventRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideTournamentRepository(api: SofascoreApi): TournamentRepository {
        return TournamentRepositoryImpl(api)
    }
}
