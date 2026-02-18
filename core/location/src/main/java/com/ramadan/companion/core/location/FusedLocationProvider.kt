package com.ramadan.companion.core.location

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.ramadan.companion.domain.location.LocationProvider
import com.ramadan.companion.domain.location.model.Location
import kotlinx.coroutines.suspendCancellableCoroutine
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * LocationProvider implementation using FusedLocationProviderClient.
 */
class FusedLocationProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationProvider {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Location = suspendCancellableCoroutine { cont ->
        val client = LocationServices.getFusedLocationProviderClient(context)
        val token = CancellationTokenSource()
        cont.invokeOnCancellation { token.cancel() }
        client.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, token.token)
            .addOnSuccessListener { androidLocation ->
                if (androidLocation != null) {
                    cont.resume(Location(androidLocation.latitude, androidLocation.longitude))
                } else {
                    cont.resumeWithException(SecurityException("Location unavailable"))
                }
            }
            .addOnFailureListener { cont.resumeWithException(it) }
    }
}
