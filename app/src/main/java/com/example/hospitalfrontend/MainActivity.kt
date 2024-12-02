package com.example.hospitalfrontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            onClick = { onScreenSelected("Find") }, enabled = currentScreen != "Find"
        ) {
            Text("Find")
        }
    }
    when (currentScreen) {
        "Find" -> FindScreen()
    }
}