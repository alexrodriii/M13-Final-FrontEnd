package com.example.hospitalfrontend.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.hospitalfrontend.ui.nurses.view.RoomDetailScreen
import androidx.compose.runtime.getValue
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
    val loginState by nurseViewModel.loginState.collectAsState()
    val navController = rememberNavController()
    val startDestination = if (loginState.isLogin) "home" else "login"
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {



    }
}

