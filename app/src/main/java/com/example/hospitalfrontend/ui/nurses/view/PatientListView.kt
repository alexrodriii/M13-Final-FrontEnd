package com.example.hospitalfrontend.ui.nurses.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hospitalfrontend.model.PatientState
import com.example.hospitalfrontend.network.RemoteApiMessageListPatient
import com.example.hospitalfrontend.network.RemoteViewModel

@Composable
fun PatientListView(remoteViewModel: RemoteViewModel) {
    LaunchedEffect(Unit) {
        remoteViewModel.getAllPatients()
    }

    val patientState = remoteViewModel.remoteApiMessageListPatient.value

    when (patientState) {
        is RemoteApiMessageListPatient.Loading -> {
            CircularProgressIndicator()
        }
        is RemoteApiMessageListPatient.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                items(patientState.data) { patient ->
                    Text(patient.name)
                }
            }
        }
        is RemoteApiMessageListPatient.Error -> {
            Text("Error al cargar pacientes")
        }
        else -> {
            Text("Cargando datos...")
        }
    }
}
