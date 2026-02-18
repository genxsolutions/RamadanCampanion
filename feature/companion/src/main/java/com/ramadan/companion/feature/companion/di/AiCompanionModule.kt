package com.ramadan.companion.feature.companion.di

import com.ramadan.companion.domain.companion.AiCompanionRepository
import com.ramadan.companion.feature.companion.data.FakeAiCompanionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AiCompanionModule {

    @Binds
    @Singleton
    abstract fun bindAiCompanionRepository(
        impl: FakeAiCompanionRepository
    ): AiCompanionRepository
}

