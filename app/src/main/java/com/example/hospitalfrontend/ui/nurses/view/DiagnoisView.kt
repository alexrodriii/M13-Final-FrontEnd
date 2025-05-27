package com.example.hospitalfrontend.ui.nurses.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.hospitalfrontend.model.Diagnosis
import com.example.hospitalfrontend.ui.nurses.viewmodels.DiagnosisListState
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisScreen(
    viewModel: NurseViewModel,
    diagnosisId: Int,
    navController: NavController
) {
    val diagnosisListState by viewModel.diagnosisListState.collectAsState()

    LaunchedEffect(diagnosisId) {
        viewModel.loadDiagnosis(diagnosisId)
    }


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
            when (diagnosisListState) {
                is DiagnosisListState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                        Text("Carregant diagnostics...", modifier = Modifier.padding(top = 8.dp))
                    }
                }
                is DiagnosisListState.Success -> {
                    val diagnoses = (diagnosisListState as DiagnosisListState.Success).diagnoses
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(diagnoses) { diagnosis ->
                            DiagnosisCard(diagnosis)
                        }
                    }
                }
                is DiagnosisListState.Empty -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No s'han trobat els diagnostics d'aquest pacient.",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Button(onClick = { viewModel.loadDiagnosis(diagnosisId) }) {
                            Text("Reintentar")
                        }
                    }
                }
                is DiagnosisListState.Error -> {
                    val errorMessage = (diagnosisListState as DiagnosisListState.Error).message
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Error al carregar els diagnostics: $errorMessage",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Button(onClick = { viewModel.loadDiagnosis(diagnosisId) }) {
                            Text("Reintentar")
                        }
                    }
                }
                DiagnosisListState.Idle -> {
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
