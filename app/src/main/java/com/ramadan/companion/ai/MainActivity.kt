package com.ramadan.companion.ai

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ramadan.companion.ai.navigation.RamadanNavGraph
import com.ramadan.companion.core.ui.navigation.RamadanRoutes
import com.ramadan.companion.core.designsystem.theme.RamadanCompanionTheme
import com.ramadan.companion.core.ui.components.RamadanBottomBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        // Permission result; Today screen will use location on next refresh if granted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestLocationIfNeeded()
        setContent {
            RamadanCompanionTheme {
                RamadanCompanionScreen()
            }
        }
    }

    private fun requestLocationIfNeeded() {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (fine != PackageManager.PERMISSION_GRANTED || coarse != PackageManager.PERMISSION_GRANTED) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}

@Composable
private fun RamadanCompanionScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: RamadanRoutes.TODAY

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            RamadanBottomBar(
                currentRoute = currentRoute,
                onTabSelected = { route ->
                    if (route != currentRoute) {
                        navController.navigate(route) {
                            popUpTo(RamadanRoutes.TODAY) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        RamadanNavGraph(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}
