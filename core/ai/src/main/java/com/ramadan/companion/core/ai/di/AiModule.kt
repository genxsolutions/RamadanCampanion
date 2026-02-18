package com.ramadan.companion.core.ai.di

import com.ramadan.companion.core.ai.AIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {

    private const val OPENAI_BASE_URL = "https://api.openai.com/"

    @Provides
    @Singleton
    @Named("aiOkHttpClient")
    fun provideAiOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideAIService(@Named("aiOkHttpClient") client: OkHttpClient): AIService = Retrofit.Builder()
        .baseUrl(OPENAI_BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(AIService::class.java)
}
