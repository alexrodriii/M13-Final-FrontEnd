package com.example.hospitalfrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.hospitalfrontend.ui.login.*
import com.example.hospitalfrontend.ui.login.viewmodels.LoginViewModel
import com.example.hospitalfrontend.ui.nurses.view.*
import com.example.hospitalfrontend.ui.nurses.viewmodels.FindByNameViewModel
import com.example.hospitalfrontend.ui.theme.HospitalFrontEndTheme
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HospitalFrontEndTheme {
                MyAppHomePage(nurseViewModel = NurseViewModel(), loginViewModel = LoginViewModel(), findByNameViewModel = FindByNameViewModel())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePage() {
    HospitalFrontEndTheme {
        MyAppHomePage(
            nurseViewModel = NurseViewModel(),loginViewModel = LoginViewModel(), findByNameViewModel = FindByNameViewModel()
        )
    }
}

@Composable
fun MyAppHomePage(nurseViewModel: NurseViewModel, loginViewModel: LoginViewModel, findByNameViewModel: FindByNameViewModel) {
    var nextScreen by rememberSaveable { mutableStateOf("Home") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (nextScreen == "Home") {
            HomeScreen { selectedScreen -> nextScreen = selectedScreen }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Button "Back" with margin
                Button(
                    onClick = { nextScreen = "Home" },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(8.dp)
                ) {
                    Text("Back")
                }

                // Content of the application selected
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    when (nextScreen) {
                        "List" -> ListNurseScreen(nurseViewModel = nurseViewModel)
                        "Login" -> HospitalLoginScreen(loginViewModel= loginViewModel)
                        "Find" -> FindScreen(viewModel = findByNameViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onScreenSelected: (String) -> Unit) {
    val options = listOf("Find", "List", "Login") // Options of navigation

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Add the Logo of Hospital
        androidx.compose.foundation.Image(
            painter = painterResource(id = R.drawable.logo_hospital),
            contentDescription = "Logo Hospital"
        )
        // Add a text
        Text(
            text = "Hospital Menu",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        // Create the different buttons dynamic
        options.forEach { option ->
            ButtonMenuHome(onScreenSelected = { onScreenSelected(option) }, textButton = option)
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