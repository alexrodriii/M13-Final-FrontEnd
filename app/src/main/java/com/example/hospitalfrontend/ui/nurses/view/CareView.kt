// M13-Final-FrontEnd/app/src/main/java/com/example/hospitalfrontend/ui/nurses/view/CareView.kt
package com.example.hospitalfrontend.ui.nurses.view

import android.util.Log
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel // Importar viewModel
import com.example.hospitalfrontend.network.RemoteViewModel // Importar RemoteViewModel
import com.example.hospitalfrontend.network.RemoteApiMessageListCare // Importar el Sealed Class de Care
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import com.example.hospitalfrontend.navigation.AppScreen
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareView(
    navController: NavController,
    patientId: Int?,
    remoteViewModel: RemoteViewModel = viewModel(),
    roomId: String?
) {
    val caresState = remoteViewModel.caresByPatient
    LaunchedEffect(patientId) {
        patientId?.let { id ->
            remoteViewModel.getCaresByPatient(id)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalls de Care del pacient amb ID: ${patientId ?: "N/A"}") },
                    navigationIcon = {
                        IconButton(onClick = {

                            roomId?.let {
                                navController.navigate("roomDetail/${roomId}") {

                                    popUpTo(AppScreen.RoomDetailScreen.route) {
                                        inclusive = true
                                    }
                                }
                            } ?: navController.popBackStack()
                        }) {
                            Icon(Icons.Filled.ArrowBack, "Tornar als detalls de l'habitació")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    patientId?.let {
                        navController.navigate("careAdd/${patientId}/${roomId}")
                    }
                }) {
                    Icon(Icons.Filled.Add, "Afegeix una Care nova")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (caresState) {
                    is RemoteApiMessageListCare.Loading -> {
                        CircularProgressIndicator()
                        Text("Carregant detalls de la Care", modifier = Modifier.padding(top = 8.dp))
                    }
                    is RemoteApiMessageListCare.Success -> {
                        val cares = caresState.care
                        Text(
                            text = "Llista de les Cares:",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        if (cares.isEmpty()) {
                            Text("No s'han trobat registres de Cares per a aquest pacient.", style = MaterialTheme.typography.bodyLarge)
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(cares) { care ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth().clickable { // Añadir el modificador clickable
                                            care.id?.let {
                                                navController.navigate("careDetail/${care.id}")
                                            }
                                        },
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer) // Color diferente para distinguir

                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            care.date?.let { date ->
                                                val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
                                                dateFormatter.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));

                                                Text(
                                                    text = "Date: ${dateFormatter.format(date)}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                                                    fontStyle = FontStyle.Italic
                                                )
                                            }
                                            care.nurse?.let { nurse ->
                                                val nurseFullName = if (nurse.surname.isNullOrBlank()) {
                                                    nurse.name
                                                } else {
                                                    "${nurse.name} ${nurse.surname}"
                                                }
                                                Text(
                                                    text = "Infermera: ${nurse.name} ${nurse.surname ?: ""}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                                                    fontStyle = FontStyle.Italic
                                                )
                                            } ?: Text(
                                                text = "Infermera no ha sigut trobada",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f),
                                                fontStyle = FontStyle.Italic
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    is RemoteApiMessageListCare.Error -> {
                        Text("Error: ${caresState.errorMessage}", color = MaterialTheme.colorScheme.error)
                    }
                    RemoteApiMessageListCare.Idle -> {
                        Text("Esperant els detalls de Care...", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}