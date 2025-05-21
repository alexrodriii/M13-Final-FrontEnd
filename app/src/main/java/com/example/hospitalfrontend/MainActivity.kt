package com.example.hospitalfrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.hospitalfrontend.network.RemoteViewModel
import com.example.hospitalfrontend.ui.login.HospitalLoginScreen
import com.example.hospitalfrontend.ui.nurses.view.*
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel
import com.example.hospitalfrontend.ui.theme.HospitalFrontEndTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HospitalFrontEndTheme {
                MyAppHomePage(
                    nurseViewModel = NurseViewModel(), remoteViewModel = RemoteViewModel()
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomePage() {
    HospitalFrontEndTheme {
        MyAppHomePage(
            nurseViewModel = NurseViewModel(), remoteViewModel = RemoteViewModel()
        )
    }
}

@Composable
fun MyAppHomePage(
    nurseViewModel: NurseViewModel, remoteViewModel: RemoteViewModel
) {
    // Set up the NavController for navigation
    val navController = rememberNavController()

    // Observe the login state as a StateFlow
    val loginState by nurseViewModel.loginState.collectAsState()
    // Determines the initial screen according to the authentication status

    val startDestination = if (loginState.isLogin) "room" else "login"

    LaunchedEffect(loginState.isLogin) {
        if (loginState.isLogin) {
            navController.navigate("room") {
                popUpTo("login") { inclusive = true }
            }
        } else{
            navController.navigate("login") {
                popUpTo("room") { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable("diagnosis/{patientId}") { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId")?.toIntOrNull()
            if (patientId != null) {
                DiagnosisScreen(
                    viewModel = nurseViewModel,
                    diagnosisId = patientId,
                    navController = navController
                )
            } else {
                Text("Invalid patient ID")
            }
        }
        composable("room") {
                       RoomScreen(remoteViewModel = remoteViewModel, navController = navController)
        }
        composable("roomDetail/{roomId}") { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId")
            RoomDetailScreen(remoteViewModel = remoteViewModel,navController = navController, roomId = roomId)
        }
        composable("login") {
            HospitalLoginScreen(
                navController = navController,
                nurseViewModel = nurseViewModel,
                remoteViewModel = remoteViewModel
            )
        }
    }
}