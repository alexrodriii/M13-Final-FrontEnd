package com.example.hospitalfrontend.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hospitalfrontend.SplashScreen
import com.example.hospitalfrontend.network.RemoteViewModel
import com.example.hospitalfrontend.ui.login.LoginOrRegisterScreen
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel

@Composable
fun AppNavigation(
    nurseViewModel: NurseViewModel,
    remoteViewModel: RemoteViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreen.SplashScreen.route
    ) {
        composable(AppScreen.SplashScreen.route) {
            SplashScreen {
                navController.navigate(AppScreen.LoginOrRegisterScreen.route) {
                    popUpTo(AppScreen.SplashScreen.route) { inclusive = true }
                }
            }
        }
        composable(AppScreen.LoginOrRegisterScreen.route) {
            LoginOrRegisterScreen(navController, nurseViewModel, remoteViewModel)
        }
    }
}

