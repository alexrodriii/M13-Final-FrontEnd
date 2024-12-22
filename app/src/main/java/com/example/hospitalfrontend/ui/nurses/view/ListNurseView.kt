package com.example.hospitalfrontend.ui.nurses.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospitalfrontend.R
import com.example.hospitalfrontend.model.NurseState
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel
import com.example.hospitalfrontend.ui.theme.HospitalFrontEndTheme
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun ListNurseScreen(navController: NavController, nurseViewModel: NurseViewModel) {
    val nurses by nurseViewModel.nurses.collectAsState()

    // Use a Box to stack the back button and LazyColumn
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        // Back button at the top-right
        IconButton(
            onClick = {
                navController.popBackStack()
            }, modifier = Modifier
                .align(Alignment.TopEnd) // Position at top-right
                .zIndex(1f) // Ensures this is above LazyColumn
        ) {
            Icon(
                imageVector = Icons.Filled.Close, // Example icon
                contentDescription = "Close Button", tint = colorResource(id = R.color.colorText)
            )
        }

        // LazyColumn for listing nurses
        LazyColumn(
            modifier = Modifier.fillMaxSize(), // Fill the rest of the screen
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 40.dp)
        ) {
            items(items = nurses, itemContent = { nurse ->
                NurseListItem(nurse = nurse)
            })
        }
    }
}


@Composable
fun NurseListItem(nurse: NurseState) {
    val age by remember(nurse.age) { // Calculate age only when nurse.age changes
        mutableIntStateOf(calculateAge(nurse.age))
    }
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.nurse_profile),
                contentDescription = "Image Profile",
                modifier = Modifier.size(50.dp)

            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = "ID Nurse: ${nurse.id}")
                Text(text = "Name: ${nurse.name}")
                Text(text = "Surname: ${nurse.surname}")
                Text(text = "Date Bird: ${nurse.age} ($age years old)")
                Text(text = "Email: ${nurse.email}")
                Text(text = "Speciality: ${nurse.speciality}")
            }
        }
    }
}

fun calculateAge(birthDate: String): Int {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val birthDateLocalDate = LocalDate.parse(birthDate, formatter)
        val currentDate = LocalDate.now()
        Period.between(birthDateLocalDate, currentDate).years
    } catch (e: Exception) {
        Log.e("CalculateAge", "Invalid birth date: $birthDate", e)
        0 // Default to 0 if there's an error
    }
}


// Preview
@Preview(showBackground = true)
@Composable
fun ListPage() {
    HospitalFrontEndTheme {
        val navController = rememberNavController()

        ListNurseScreen(
            navController, nurseViewModel = NurseViewModel()
        )
    }
}

