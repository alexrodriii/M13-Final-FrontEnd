package com.example.hospitalfrontend.ui.nurses.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel

@Composable
fun DiagnosisScreen(viewModel: NurseViewModel) {
    LaunchedEffect(Unit) {
        viewModel.loadDiagnosis(1)
    }
    val diagnosis = viewModel.diagnosisState
    val error = viewModel.errorMessage

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        if (diagnosis != null) {
            Text(text = "Diagnóstico: ${diagnosis.diagnostico}", fontSize = 20.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Motivo: ${diagnosis.motivo}", fontSize = 16.sp)
        } else if (error != null) {
            Text(text = error, fontSize = 18.sp)
        } else {
            CircularProgressIndicator()
        }
    }
}