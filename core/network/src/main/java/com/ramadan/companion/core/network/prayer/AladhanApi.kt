package com.ramadan.companion.core.network.prayer

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Aladhan.com prayer times API (free, no key).
 * https://aladhan.com/prayer-times-api
 */
interface AladhanApi {

    @GET("v1/timings")
    suspend fun getTimings(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("date") date: String // dd-MM-yyyy
    ): AladhanResponse
}

@Serializable
data class AladhanResponse(
    val data: AladhanData
)

@Serializable
data class AladhanData(
    val timings: AladhanTimings,
    val date: AladhanDate
)

@Serializable
data class AladhanTimings(
    val Fajr: String,
    val Dhuhr: String,
    val Asr: String,
    val Maghrib: String,
    val Isha: String
)

@Serializable
data class AladhanDate(
    val readable: String
)
