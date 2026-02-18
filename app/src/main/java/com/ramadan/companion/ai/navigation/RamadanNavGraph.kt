package com.ramadan.companion.ai.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ramadan.companion.feature.companion.presentation.CompanionScreen
import com.ramadan.companion.feature.quran.presentation.QuranScreen
import com.ramadan.companion.feature.reflection.presentation.ReflectionScreen
import com.ramadan.companion.feature.today.presentation.TodayScreen
import com.ramadan.companion.core.ui.navigation.RamadanRoutes

@Composable
fun RamadanNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = RamadanRoutes.TODAY,
        modifier = modifier
    ) {
        composable(RamadanRoutes.TODAY) {
            TodayScreen(
                viewModel = hiltViewModel(),
                onNavigateToCompanion = { navController.navigate(RamadanRoutes.COMPANION) }
            )
        }
        composable(RamadanRoutes.COMPANION) {
            CompanionScreen()
        }
        composable(RamadanRoutes.QURAN) {
            QuranScreen()
        }
        composable(RamadanRoutes.REFLECTION) {
            ReflectionScreen()
        }
    }
}
