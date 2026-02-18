package com.ramadan.companion.domain.location

import com.ramadan.companion.domain.location.model.Location

/**
 * Abstraction for obtaining current device location.
 * Implemented in data layer (e.g. FusedLocationProviderClient).
 */
interface LocationProvider {
    suspend fun getCurrentLocation(): Location
}
