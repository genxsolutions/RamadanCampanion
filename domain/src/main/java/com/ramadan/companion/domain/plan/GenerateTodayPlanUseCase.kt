package com.ramadan.companion.domain.plan

import com.ramadan.companion.domain.plan.model.TodayPlan
import com.ramadan.companion.domain.plan.model.UserContext
import com.ramadan.companion.domain.plan.repository.AiPlannerRepository
import javax.inject.Inject

/**
 * Delegates to repository; no business logic here.
 * Repository handles AI call and fallback heuristic.
 */
class GenerateTodayPlanUseCase @Inject constructor(
    private val repository: AiPlannerRepository
) {

    suspend operator fun invoke(context: UserContext): TodayPlan =
        repository.generateTodayPlan(context)
}
