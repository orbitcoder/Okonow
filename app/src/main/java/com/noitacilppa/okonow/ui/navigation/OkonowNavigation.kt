package com.noitacilppa.okonow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noitacilppa.okonow.data.UserPreferences
import com.noitacilppa.okonow.ui.main.MainShell
import com.noitacilppa.okonow.ui.onboarding.OnboardingScreen

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Main : Screen("main")
}

@Composable
fun OkonowNavHost(initialUserName: String?) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    
    // userName is collected here to respond to changes (like logout), 
    // but we use the passed initialUserName for the start destination.
    val userName by userPreferences.userName.collectAsState(initial = initialUserName)

    // If userName is null or blank, show onboarding. Otherwise, go straight to Main.
    val startDestination = if (userName.isNullOrBlank()) {
        Screen.Onboarding.route
    } else {
        Screen.Main.route
    }

    // We use a unique key for NavHost based on startDestination to force a restart 
    // of the graph when the login state changes (e.g. during logout).
    key(startDestination) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(Screen.Onboarding.route) {
                OnboardingScreen(navController = navController)
            }
            composable(Screen.Main.route) {
                MainShell()
            }
        }
    }
}
