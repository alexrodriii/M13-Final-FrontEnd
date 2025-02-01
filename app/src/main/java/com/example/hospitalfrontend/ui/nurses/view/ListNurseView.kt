package com.example.hospitalfrontend.ui.nurses.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.hospitalfrontend.R
import com.example.hospitalfrontend.model.NurseState
import com.example.hospitalfrontend.network.RemoteViewModel
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter

@Composable
fun ListNurseScreen(
    navController: NavController,
    nurseViewModel: NurseViewModel,
    isError: MutableState<Boolean>,
    remoteViewModel: RemoteViewModel
) {
    val nurses by nurseViewModel.nurses.collectAsState()
    //Pop up error
    if (isError.value) {
        AlertDialog(
            onDismissRequest = { isError.value = false },
            confirmButton = {
                TextButton(onClick = { isError.value = false }) {
                    Text("OK")
                }
            },
            title = {
                Text(text = "Error: List Nurse", color = Color.Red)
            },
            text = {
                Text(text = "Failing into fetching data of list nurses")
            }
        )
    }

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
                NurseListItem(nurse = nurse, remoteViewModel)
            })
        }
    }
}

@Composable
fun NurseListItem(nurse: NurseState, remoteViewModel: RemoteViewModel) {
    val age by remember(nurse.age) { // Calculate age only when nurse.age changes
        mutableIntStateOf(calculateAge(nurse.age))
    }
    val profileImageBitmap = remoteViewModel.getCachedPhoto(nurse.id)

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            if (profileImageBitmap != null) {
                Image(
                    bitmap = profileImageBitmap.asImageBitmap(),
                    contentDescription = "Profile Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.nurse_profile),
                    contentDescription = "Default Profile Image",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                )
            }

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
