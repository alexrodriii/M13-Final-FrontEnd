package com.example.hospitalfrontend.ui.nurses.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hospitalfrontend.navigation.AppScreen
import com.example.hospitalfrontend.network.RemoteApiMessageListPatient
import com.example.hospitalfrontend.network.RemoteViewModel
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel
import com.example.hospitalfrontend.ui.nurses.view.*

@Composable
fun RoomDetailScreen(
    remoteViewModel: RemoteViewModel = viewModel(),
    nurseViewModel: NurseViewModel = viewModel(),
    navController: NavController,
    roomId: String?
) {
    val patientsState = remoteViewModel.patientsByRoom1

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
            // Close icon
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Butó tancar"
                )
            }
            Text(
                text = "Detalls de l'habitació",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
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
                        text = "No s'han trobat pacients.",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                is RemoteApiMessageListPatient.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        items(state.patients) { patient ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {

                                    Text(
                                        "Nom: ${patient.name}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Text(
                                        "DNI: ${patient.dni}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        "Telèfon: ${patient.telefono}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        "Email: ${patient.correo}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        "Adreça: ${patient.direccion}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        "Numero historial: ${patient.historyNumber}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = {
                                            navController.navigate("diagnosis/${patient.id}")
                                        }
                                    ) {
                                        Text("Veure Diagnòstic", fontSize = 16.sp)
                                    }
                                    Button(
                                        onClick = {
                                            roomId?.let {
                                                navController.navigate("care/${patient.id}/${roomId}")
                                            }
                                        }
                                    ) {
                                        Text("Veure Care",  fontSize = 16.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                RemoteApiMessageListPatient.Idle -> TODO()
            }
        }
    }
}
