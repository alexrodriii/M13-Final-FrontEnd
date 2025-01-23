package com.example.hospitalfrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
    val startDestination = if (loginState.isLogin) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("home") {
            HomeScreen(
                navController = navController,
                nurseViewModel = nurseViewModel
            )
        }
        composable("list") {
            ListNurseScreen(navController = navController, nurseViewModel = nurseViewModel)
        }
        composable("find") {
            FindScreen(navController = navController, nurseViewModel = nurseViewModel)
        }
        composable("login") {
            HospitalLoginScreen(
                navController = navController,
                nurseViewModel = nurseViewModel,
                remoteViewModel = remoteViewModel
            )
        }
        composable("create") {
            CreateNursePage(navController = navController, nurseViewModel = nurseViewModel, remoteViewModel = remoteViewModel)

        }
    }
}

@Composable
fun HomeScreen(navController: NavController, nurseViewModel: NurseViewModel) {
    val options = listOf("Find", "List") // Show Find and List when logged in

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_hospital),
            contentDescription = "Logo Hospital"
        )
        Text(
            text = "Hospital Menu",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        options.forEach { option ->
            ButtonMenuHome(
                onScreenSelected = { navController.navigate(option.lowercase()) },
                textButton = option
            )
        }

        Button(
            onClick = {
                nurseViewModel.disconnectNurse() // Call the disconnect method
                navController.navigate("home") // Navigate back to home
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(8.dp)
        ) {
            Text("Logout")
        }
    }
}


@Composable
fun ButtonMenuHome(onScreenSelected: () -> Unit, textButton: String) {
    Button(
        onClick = onScreenSelected, modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(8.dp)
    ) {
        Text(textButton)
    }
}
