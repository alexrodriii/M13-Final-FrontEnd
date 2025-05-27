package com.example.hospitalfrontend.ui.nurses.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hospitalfrontend.model.Diagnosis
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisScreen(
    viewModel: NurseViewModel,
    diagnosisId: Int,
    navController: NavController
) {
    LaunchedEffect(diagnosisId) {
        viewModel.loadDiagnosis(diagnosisId)
    }

    val diagnosisList = viewModel.diagnosisState
    val error = viewModel.errorMessage

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Diagnòstic del pacient",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Enrere")
                    }
                }
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                diagnosisList.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(diagnosisList) { diagnosis ->
                            DiagnosisCard(diagnosis)
                        }
                    }
                }

                error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = error,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.loadDiagnosis(diagnosisId) }) {
                            Text("Reintentar")
                        }
                    }
                }

                else -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun DiagnosisCard(diagnosis: Diagnosis) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Diagnòstic:", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
            Text(diagnosis.diagnostico, fontSize = 14.sp, modifier = Modifier.padding(bottom = 12.dp))

            Text("Motiu:", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
            Text(diagnosis.motivo, fontSize = 14.sp, modifier = Modifier.padding(bottom = 12.dp))

            Text("Portador d'O2:", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
            Text(diagnosis.portadorO2Tipus ?: "N/A", fontSize = 14.sp, modifier = Modifier.padding(bottom = 12.dp))

            Text("Portador de Bolquer:", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
            Text(if (diagnosis.portadorBolquer == true) "Sí" else "No", fontSize = 14.sp, modifier = Modifier.padding(bottom = 12.dp))

            Text("Número de Canvis de Bolquer:", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
            Text(diagnosis.numeroCanvisBolquer?.toString() ?: "0", fontSize = 14.sp, modifier = Modifier.padding(bottom = 12.dp))

            Text("Estat de la Pell:", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.primary)
            Text(diagnosis.estatPell ?: "No disponible", fontSize = 14.sp)
        }
    }
}
