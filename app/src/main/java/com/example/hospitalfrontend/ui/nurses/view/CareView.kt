// M13-Final-FrontEnd/app/src/main/java/com/example/hospitalfrontend/ui/nurses/view/CareView.kt
package com.example.hospitalfrontend.ui.nurses.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareView(
    navController: NavController,
    patientId: Int?,
    remoteViewModel: RemoteViewModel = viewModel() // Inyectar RemoteViewModel
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
                    title = { Text("Care Details for Patient ID: ${patientId ?: "N/A"}") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Filled.ArrowBack, "Back")
                        }
                    }
                )
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
                        Text("Loading care details...", modifier = Modifier.padding(top = 8.dp))
                    }
                    is RemoteApiMessageListCare.Success -> {
                        val cares = caresState.care
                        Text(
                            text = "List of Cares:",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        if (cares.isEmpty()) {
                            Text("No care records found for this patient.", style = MaterialTheme.typography.bodyLarge)
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(cares) { care ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer) // Color diferente para distinguir
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text("Care ID: ${care.id ?: "N/A"}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                            Text("Pols: ${care.pols}", style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                            Text("Frequencia respiratoria: ${care.freq_resp}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                            Text("Temperatura: ${care.temperatura}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Normal, color = MaterialTheme.colorScheme.onSecondaryContainer)

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
                        Text("Waiting for care details...", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}