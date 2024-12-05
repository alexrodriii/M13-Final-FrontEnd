package com.example.hospitalfrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import com.example.hospitalfrontend.ui.login.*
import com.example.hospitalfrontend.ui.nurses.view.*

import com.example.hospitalfrontend.ui.theme.HospitalFrontEndTheme
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HospitalFrontEndTheme {
                MyAppHomePage(nurseViewModel = NurseViewModel())
            }
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun HomePage() {
    HospitalFrontEndTheme {
        MyAppHomePage(
            nurseViewModel = NurseViewModel()
        )
    }
}

@Composable
fun MyAppHomePage(nurseViewModel: NurseViewModel) {
    var nextScreen by rememberSaveable { mutableStateOf("Home") }

    Column(
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        // Put the navigation screen static
        BottomNavigationBar(
            currentScreen = nextScreen,
            onScreenSelected = { selectedScreen -> nextScreen = selectedScreen },
            nurseViewModel = nurseViewModel
        )
    }
}

@Composable
fun BottomNavigationBar(
    currentScreen: String,
    onScreenSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    nurseViewModel: NurseViewModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { onScreenSelected("Home") }, enabled = currentScreen != "Home"
        ) {
            Text("Home")
        }
        Button(

            onClick = { onScreenSelected("Find") }, enabled = currentScreen != "Find"
        ) {
            Text("Find")
        }
        Button(
            onClick = { onScreenSelected("List") }, enabled = currentScreen != "List"
        ) {
            Text("List")
        }
        Button(
            onClick = { onScreenSelected("Login") }, enabled = currentScreen != "Login"
        ) {
            Text("Login")
        }
    }
    when (currentScreen) {
        "List" -> ListNurseScreen(nurseViewModel = nurseViewModel)
        "Login" -> HospitalLoginScreen()
        "Find" -> FindScreen()
    }
}