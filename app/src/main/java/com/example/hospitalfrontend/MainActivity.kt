package com.example.hospitalfrontend

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hospitalfrontend.network.RemoteApiMessageListNurse
import com.example.hospitalfrontend.network.RemoteViewModel
import com.example.hospitalfrontend.ui.login.HospitalLoginScreen
import com.example.hospitalfrontend.ui.nurses.view.CreateNursePage
import com.example.hospitalfrontend.ui.nurses.view.FindScreen
import com.example.hospitalfrontend.ui.nurses.view.ListNurseScreen
import com.example.hospitalfrontend.ui.nurses.view.ProfileScreen
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel
import com.example.hospitalfrontend.ui.theme.HospitalFrontEndTheme

class MainActivity : ComponentActivity() {
    private val nurseViewModel: NurseViewModel by viewModels()
    private val remoteViewModel: RemoteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HospitalFrontEndTheme {
                //AppNavigation(nurseViewModel, remoteViewModel)
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
            nurseViewModel = NurseViewModel(),
            remoteViewModel = RemoteViewModel()
        )
    }
}

@Composable

fun MyAppHomePage(
    nurseViewModel: NurseViewModel, remoteViewModel: RemoteViewModel
) {
    val remoteApiMessageListNurse = remoteViewModel.remoteApiListMessage.value
    // Set up the NavController for navigation
    val navController = rememberNavController()

    // Observe the login state as a StateFlow
    val loginState by nurseViewModel.loginState.collectAsState()

    // Determines the initial screen according to the authentication status
    val startDestination = if (loginState.isLogin) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {

        composable("create") {
            CreateNursePage(
                navController = navController,
                nurseViewModel = nurseViewModel,
                remoteViewModel = remoteViewModel
            )

        }
        composable("find") {
            FindScreen(
                navController = navController,
                nurseViewModel = nurseViewModel,
                remoteApiMessage = remoteViewModel
            )
        }
        composable("home") {
            HomeScreen(
                navController = navController,
                nurseViewModel = nurseViewModel
            )
        }
        composable("list") {
            //Variable for the error
            val isError = remember { mutableStateOf(false) }
            //Shows us the answer about the request to the API
            LaunchedEffect(Unit) {
                remoteViewModel.getAllNurses()
            }

            when (remoteApiMessageListNurse) {
                is RemoteApiMessageListNurse.Success -> {
                    nurseViewModel.loadNurses(remoteApiMessageListNurse.message)
                }

                is RemoteApiMessageListNurse.Error -> {
                    Log.d("List Error", "Error")
                    isError.value = true
                }

                is RemoteApiMessageListNurse.Loading -> {
                    Log.d("List", "Loading List")

                }

            }
            ListNurseScreen(
                navController = navController,
                nurseViewModel = nurseViewModel,
                isError = isError
            )
        }
        composable("login") {
            HospitalLoginScreen(
                navController = navController,
                nurseViewModel = nurseViewModel,
                remoteViewModel = remoteViewModel
            )
        }
        composable("profile") {
            ProfileScreen(navController = navController, nurseViewModel = nurseViewModel, remoteViewModel= remoteViewModel)
        }
    }
}

@Composable
fun HomeScreen(navController: NavController, nurseViewModel: NurseViewModel) {
    val options = listOf("Find", "List", "Profile") // Show Find and List when logged in

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_hospital_app),
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

