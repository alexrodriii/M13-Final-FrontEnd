package com.example.hospitalfrontend.ui.nurses.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hospitalfrontend.network.RemoteApiMessageListPatient
import com.example.hospitalfrontend.network.RemoteViewModel

@Composable
fun RoomDetailScreen(
    remoteViewModel: RemoteViewModel = viewModel(),
    navController: NavController,
    roomId: String?
) {
    val patientsState = remoteViewModel.patientsByRoom

    LaunchedEffect(roomId) {
        roomId?.toIntOrNull()?.let { id ->
            remoteViewModel.getPatientsByRoom(id)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close Button"
                )
            }

            Text(
                "Room Detail",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )

            when (val state = patientsState.value) {
                is RemoteApiMessageListPatient.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is RemoteApiMessageListPatient.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                is RemoteApiMessageListPatient.Empty -> {
                    Text(
                        text = "No patients found.",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                is RemoteApiMessageListPatient.Success -> {
                    LazyColumn {
                        items(state.patients) { patient ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("ID: ${patient.id}", fontSize = 16.sp)
                                    Text("Name: ${patient.name}", fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
