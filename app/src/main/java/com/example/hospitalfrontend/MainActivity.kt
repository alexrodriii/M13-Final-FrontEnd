package com.example.hospitalfrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hospitalfrontend.ui.*
import com.example.hospitalfrontend.ui.theme.HospitalFrontEndTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HospitalFrontEndTheme {
                MyAppHomePage()
            }
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun HomePage() {
    HospitalFrontEndTheme {
        MyAppHomePage()
    }
}

@Composable
fun MyAppHomePage() {
    var nextScreen by rememberSaveable { mutableStateOf("Home") }

    Column(
        modifier = Modifier
            .padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        // Put the navigation screen static
        BottomNavigationBar(currentScreen = nextScreen,
            onScreenSelected = { selectedScreen -> nextScreen = selectedScreen })
    }
}

@Composable
fun BottomNavigationBar(
    currentScreen: String, onScreenSelected: (String) -> Unit, modifier: Modifier = Modifier
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
            onClick = { onScreenSelected("Login") }, enabled = currentScreen != "Login"
        ) {
            Text("Login")
        }
        Button(
            onClick = { onScreenSelected("List Nurse") }, enabled = currentScreen != "List Nurse"
        ) {
            Text("List Nurse")
        }
    }
    when (currentScreen) {
        "List Nurse" -> ListNurseScreen()
        "Login" -> HospitalLoginScreen()
    }
}