package com.example.hospitalfrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.hospitalfrontend.ui.login.HospitalLoginScreen
import com.example.hospitalfrontend.ui.login.viewmodels.LoginViewModel
import com.example.hospitalfrontend.ui.nurses.view.*
import com.example.hospitalfrontend.ui.theme.HospitalFrontEndTheme
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Use viewModel() to retain ViewModel instances across recompositions
            val nurseViewModel: NurseViewModel = viewModel()
            val loginViewModel: LoginViewModel = viewModel { LoginViewModel(nurseViewModel) }

            HospitalFrontEndTheme {
                MyAppHomePage(nurseViewModel, loginViewModel)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomePage() {
    HospitalFrontEndTheme {
        val nurseViewModel = NurseViewModel()
        val loginViewModel = LoginViewModel(nurseViewModel)

        MyAppHomePage(
            nurseViewModel, loginViewModel
        )
    }
}

@Composable
fun MyAppHomePage(
    nurseViewModel: NurseViewModel, loginViewModel: LoginViewModel
) {
    // Set up the NavController for navigation
    val navController = rememberNavController()

    // Observe the login state as a StateFlow
    val loginState by loginViewModel.loginState.collectAsState()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(isLoggedIn = loginState.isLogin, navController = navController)
        }
        composable("list") {
            ListNurseScreen(navController = navController, nurseViewModel = nurseViewModel)
        }
        composable("find") {
            FindScreen(navController = navController, nurseViewModel = nurseViewModel)
        }
        composable("login") {
            HospitalLoginScreen(navController = navController, loginViewModel = loginViewModel, nurseViewModel = nurseViewModel)
        }
        composable("create") {
            CreateNursePage(navController = navController, loginViewModel = loginViewModel)
        }
    }
}


@Composable
fun HomeScreen(isLoggedIn: Boolean, navController: NavController) {
    val options = if (isLoggedIn) {
        listOf("Find", "List") // Show Find and List when logged in
    } else {
        listOf("Login", "Create") // Show Login and Create when not logged in
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        androidx.compose.foundation.Image(
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
