package com.ramadan.companion.feature.today.data.time

import com.ramadan.companion.domain.time.Clock
import java.util.Calendar
import javax.inject.Inject

/**
 * System clock for current time (minutes since midnight) and date key.
 */
class SystemClock @Inject constructor() : Clock {

    override fun currentMinutesOfDay(): Int {
        val c = Calendar.getInstance()
        return c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE)
    }

    override fun todayDateKey(): String {
        val c = Calendar.getInstance()
        return String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH))
    }
}
