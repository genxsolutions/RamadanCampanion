package com.ramadan.companion.feature.today.di

import com.ramadan.companion.domain.plan.repository.AiPlannerRepository
import com.ramadan.companion.domain.prayer.repository.PrayerTimeRepository
import com.ramadan.companion.domain.time.Clock
import com.ramadan.companion.feature.today.data.plan.AiPlannerRepositoryImpl
import com.ramadan.companion.feature.today.data.prayer.PrayerTimeRepositoryImpl
import com.ramadan.companion.feature.today.data.time.SystemClock
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TodayModule {

    @Binds
    @Singleton
    abstract fun bindPrayerTimeRepository(impl: PrayerTimeRepositoryImpl): PrayerTimeRepository

    @Binds
    @Singleton
    abstract fun bindClock(impl: SystemClock): Clock

    @Binds
    @Singleton
    abstract fun bindAiPlannerRepository(impl: AiPlannerRepositoryImpl): AiPlannerRepository
}
