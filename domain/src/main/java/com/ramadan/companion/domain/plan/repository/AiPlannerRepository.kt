package com.ramadan.companion.domain.plan.repository

import com.ramadan.companion.domain.plan.model.TodayPlan
import com.ramadan.companion.domain.plan.model.UserContext

/**
 * Domain contract for generating today's plan (AI or fallback).
 * API key and network details stay in data layer.
 */
interface AiPlannerRepository {

    suspend fun generateTodayPlan(context: UserContext): TodayPlan
}
