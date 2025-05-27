// hospitalfrontend/ui/nurses/view/CareAddView.kt
package com.example.hospitalfrontend.ui.nurses.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospitalfrontend.R
import com.example.hospitalfrontend.model.CareState
import com.example.hospitalfrontend.network.RemoteApiMessageCare
import com.example.hospitalfrontend.network.RemoteViewModel
import com.example.hospitalfrontend.ui.theme.HospitalFrontEndTheme
import com.example.hospitalfrontend.ui.theme.Primary
import com.example.hospitalfrontend.ui.theme.Secundary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCareView(
    navController: NavController,
    patientId: Int?,
    roomId: String?,
    remoteViewModel: RemoteViewModel = viewModel()
) {
    val taSistolica = rememberSaveable { mutableStateOf("") }
    val freqResp = rememberSaveable { mutableStateOf("") }
    val pols = rememberSaveable { mutableStateOf("") }
    val temperatura = rememberSaveable { mutableStateOf("") }

    val createCareState = remoteViewModel.createCareState
    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
    var showErrorDialog by rememberSaveable { mutableStateOf(false) }
    var dialogMessage by rememberSaveable { mutableStateOf("") }

    val isFormValid = remember(taSistolica.value, freqResp.value, pols.value, temperatura.value) {
        taSistolica.value.toIntOrNull() != null &&
                freqResp.value.toIntOrNull() != null &&
                pols.value.toIntOrNull() != null &&
                temperatura.value.toIntOrNull() != null &&
                taSistolica.value.isNotEmpty() &&
                freqResp.value.isNotEmpty() &&
                pols.value.isNotEmpty() &&
                temperatura.value.isNotEmpty()
    }

    LaunchedEffect(createCareState) {
        when (createCareState) {
            is RemoteApiMessageCare.Success -> {
                dialogMessage = "Care creada amb èxit!"
                showSuccessDialog = true
                remoteViewModel.clearApiMessage()
                patientId?.let { pId ->
                    roomId?.let { rId -> // Asegurarse de que roomId no es nulo
                        navController.navigate("care/${pId}/${rId}") {
                            popUpTo("careAdd/${pId}/${rId}") { inclusive = true } // Elimina CareAddScreen de la pila
                        }
                    } ?: run { // Si roomId es nulo, navega sin él (comportamiento de fallback)
                        navController.navigate("care/$pId") {
                            popUpTo("careAdd/{patientId}") { inclusive = true }
                        }
                    }
                }
            }
            is RemoteApiMessageCare.Error -> {
                dialogMessage = "Error creant el care: ${createCareState.errorMessage}"
                showErrorDialog = true
                remoteViewModel.clearApiMessage() // Limpiar el estado
            }
            RemoteApiMessageCare.Loading -> {
                Log.d("CareAddView", "Carregant...")
            }
            RemoteApiMessageCare.Idle -> {
                // Estado inicial o después de limpiar
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Afegir un nou Care") },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nou registre de Care",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            CareInputField(
                valueState = taSistolica,
                labelId = "Tensió Arterial Sistòlica",
                icon = Icons.Default.HealthAndSafety,
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(16.dp))

            CareInputField(
                valueState = freqResp,
                labelId = "Freqüència Respiratòria",
                icon = Icons.Default.MonitorHeart,
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(16.dp))

            CareInputField(
                valueState = pols,
                labelId = "Pulsacions",
                icon = Icons.Default.MonitorHeart,
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(16.dp))

            CareInputField(
                valueState = temperatura,
                labelId = "Temperatura",
                icon = Icons.Default.Thermostat,
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (patientId != null) {
                        val newCare = CareState(
                            id = null,
                            ta_sistolica = taSistolica.value.toIntOrNull(),
                            freq_resp = freqResp.value.toIntOrNull(),
                            pols = pols.value.toIntOrNull(),
                            temperatura = temperatura.value.toIntOrNull()
                        )
                        remoteViewModel.createCare(patientId, newCare)
                    } else {
                        dialogMessage = "Falta de ID de pacient."
                        showErrorDialog = true
                    }
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(48.dp)
                        .background(
                            brush = Brush.horizontalGradient(listOf(Secundary, Primary)),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Afegir Care",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text(text = "Èxit") },
            text = { Text(text = dialogMessage) }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            },
            title = { Text(text = "Error") },
            text = { Text(text = dialogMessage) }
        )
    }
}

@Composable
fun CareInputField(
    valueState: MutableState<String>,
    labelId: String,
    icon: ImageVector,
    keyboardType: KeyboardType,
    isSingleLine: Boolean = true
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { newValue ->
            if (keyboardType == KeyboardType.Number) {
                valueState.value = newValue.filter { it.isDigit() }
            } else {
                valueState.value = newValue
            }
        },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        leadingIcon = { Icon(imageVector = icon, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Primary,
            cursorColor = Primary,
            focusedLabelColor = Primary,
            unfocusedContainerColor = colorResource(id = R.color.white),
            unfocusedIndicatorColor = colorResource(id = R.color.colorGray)
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        isError = valueState.value.isEmpty() || valueState.value.toIntOrNull() == null
    )
    if (valueState.value.isEmpty() || valueState.value.toIntOrNull() == null) {
        Text(
            text = "Introduïu un número vàlid",
            color = Color.Red,
            fontSize = 12.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCareAddView() {
    HospitalFrontEndTheme {
        AddCareView(navController = rememberNavController(), patientId = 1, remoteViewModel = RemoteViewModel(), roomId = "1")
    }
}