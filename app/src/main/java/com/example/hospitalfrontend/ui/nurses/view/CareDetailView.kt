package com.example.hospitalfrontend.ui.nurses.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospitalfrontend.network.RemoteApiMessageCare
import com.example.hospitalfrontend.network.RemoteViewModel
import com.example.hospitalfrontend.ui.theme.HospitalFrontEndTheme
import com.example.hospitalfrontend.ui.theme.Primary // Importar Primary para colores

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CareDetailView(
    navController: NavController,
    careId: Int?,
    remoteViewModel: RemoteViewModel = viewModel()
) {
    val careDetailState = remoteViewModel.careDetailState

    LaunchedEffect(careId) {
        careId?.let { id ->
            remoteViewModel.getCareById(id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalls de Care") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Enrere")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp), // Ajustar el padding vertical y horizontal
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top // Alinear el contenido a la parte superior
        ) {
            when (val state = careDetailState) {
                is RemoteApiMessageCare.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    Text("Carregant detalls de Care...", modifier = Modifier.padding(top = 8.dp))
                }
                is RemoteApiMessageCare.Success -> {
                    val care = state.care
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(), // Ajustar la altura de la tarjeta al contenido
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), // Aumentar la elevación
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp) // Mayor padding dentro de la tarjeta
                                .fillMaxWidth()
                        ) {
                            // Título de la tarjeta
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    "Registre de Care",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary // Usar color primario
                                )

                            }
                            Divider(modifier = Modifier.padding(vertical = 12.dp)) // Separador

                            // Detalles de cuidado con iconos
                            CareDetailItem(
                                icon = Icons.Default.Favorite,
                                label = "Tensió arterial sistòlica:",
                                value = care.ta_Sistolica?.toString() ?: "N/A",
                                unit = "mmHg"
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            CareDetailItem(
                                icon = Icons.Default.Favorite,
                                label = "Tensió arterial distòlica:",
                                value = care.ta_Distolica?.toString() ?: "N/A",
                                unit = "mmHg"
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            CareDetailItem(
                                icon = Icons.Default.MonitorHeart,
                                label = "Freqüència Respiratòria:",
                                value = care.freq_resp?.toString() ?: "N/A",
                                unit = "bpm"
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            CareDetailItem(
                                icon = Icons.Default.MedicalServices,
                                label = "Pulsacions:",
                                value = care.pols?.toString() ?: "N/A",
                                unit = "ppm"
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            CareDetailItem(
                                icon = Icons.Default.Thermostat,
                                label = "Temperatura:",
                                value = care.temperatura?.toString() ?: "N/A",
                                unit = "°C"
                            )
                        }
                    }
                }
                is RemoteApiMessageCare.Error -> {
                    Text(
                        "Error: ${state.errorMessage}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
                RemoteApiMessageCare.Idle -> {
                    Text("Seleccioneu una Care per veure els detalls", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun CareDetailItem(icon: ImageVector, label: String, value: String, unit: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary, // Usar el color primario para los iconos
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
            Text(
                "$value $unit",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

