package com.ramadan.companion.ai.di

import com.ramadan.companion.ai.BuildConfig
import com.ramadan.companion.core.ai.AiApiKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @AiApiKey
    fun provideAiApiKey(): String = BuildConfig.AI_API_KEY ?: ""
}
