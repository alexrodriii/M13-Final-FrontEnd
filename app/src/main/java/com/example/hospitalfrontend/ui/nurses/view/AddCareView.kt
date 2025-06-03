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
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel
import com.example.hospitalfrontend.ui.theme.HospitalFrontEndTheme
import com.example.hospitalfrontend.ui.theme.Primary
import com.example.hospitalfrontend.ui.theme.Secundary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCareView(
    navController: NavController,
    patientId: Int?,
    roomId: String?,
    remoteViewModel: RemoteViewModel,
    nurseViewModel: NurseViewModel = viewModel()

) {
    val taSistolica = rememberSaveable { mutableStateOf("") }
    val freqResp = rememberSaveable { mutableStateOf("") }
    val pols = rememberSaveable { mutableStateOf("") }
    val temperatura = rememberSaveable { mutableStateOf("") }
    val createCareState = remoteViewModel.createCareState
    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }
    var showErrorDialog by rememberSaveable { mutableStateOf(false) }
    var dialogMessage by rememberSaveable { mutableStateOf("") }
    val ta = taSistolica.value.toIntOrNull()
    val fr = freqResp.value.toIntOrNull()
    val pulse = pols.value.toIntOrNull()
    val temp = temperatura.value.toDoubleOrNull()
    var showConfirmationDialog by rememberSaveable { mutableStateOf(false) }
    val isFormValid = remember(ta, fr, pulse, temp) {
        ta != null && fr != null && pulse != null && temp != null
    }

    /*val isFormValid = remember(taSistolica.value, freqResp.value, pols.value, temperatura.value) {
        taSistolica.value.toIntOrNull() != null &&
                freqResp.value.toIntOrNull() != null &&
                pols.value.toIntOrNull() != null &&
                temperatura.value.toIntOrNull() != null &&
                taSistolica.value.isNotEmpty() &&
                freqResp.value.isNotEmpty() &&
                pols.value.isNotEmpty() &&
                temperatura.value.isNotEmpty()
    }*/

    LaunchedEffect(createCareState) {
        when (createCareState) {
            is RemoteApiMessageCare.Success -> {
                dialogMessage = "Care creada amb èxit!"
                showSuccessDialog = true
                remoteViewModel.clearApiMessage()
                patientId?.let { pId ->
                    roomId?.let { rId ->
                        navController.navigate("care/${pId}/${rId}") {
                            popUpTo("careAdd/${pId}/${rId}") { inclusive = true }
                        }
                    } ?: run {
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
                placeholderText = "Tensió Arterial (140/90mmHg, 90/50mmHg)",
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(16.dp))

            CareInputField(
                valueState = freqResp,
                labelId = "Freqüència Respiratòria",
                placeholderText = "Freqüència Respiratòria (12x’- 20x’)",
                icon = Icons.Default.MonitorHeart,
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(16.dp))

            CareInputField(
                valueState = pols,
                labelId = "Pulsacions",
                icon = Icons.Default.MonitorHeart,
                placeholderText = "Pulsaciones (50x’-100x’)",
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(16.dp))

            CareInputField(
                valueState = temperatura,
                labelId = "Temperatura",
                placeholderText = "Temperatura(34’9ºC- 38’5ºC)",
                icon = Icons.Default.Thermostat,
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(32.dp))


            Button(
                onClick = {
                    if (isValueOutOfRange(ta, fr, pulse, temp)) {
                        showConfirmationDialog = true
                    } else {
                        submitCare(patientId, ta, fr, pulse, temp, remoteViewModel, nurseViewModel, onError = {
                            dialogMessage = it
                            showErrorDialog = true
                        })
                    }
                    /*if (patientId != null) {
                        val newCare = CareState(
                            id = null,
                            ta_sistolica = taSistolica.value.toIntOrNull(),
                            freq_resp = freqResp.value.toIntOrNull(),
                            pols = pols.value.toIntOrNull(),
                            temperatura = temperatura.value.toIntOrNull(),
                            date = null
                        )
                        remoteViewModel.createCare(patientId, newCare, nurseViewModel )
                    } else {
                        dialogMessage = "Falta de ID de pacient."
                        showErrorDialog = true
                    }*/
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

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmationDialog = false
                    submitCare(patientId, ta, fr, pulse, temp, remoteViewModel, nurseViewModel, onError = {
                        dialogMessage = it
                        showErrorDialog = true
                    })
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmationDialog = false }) {
                    Text("Cancel·la")
                }
            },
            title = { Text("Valors fora de rang") },
            text = { Text("Alguns valors estan fora del rang normal. Segur que vols continuar?") }
        )
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

fun isValueOutOfRange(
    ta: Int?, fr: Int?, pulse: Int?, temp: Double?
): Boolean {
    return (ta != null && (ta < 90 || ta > 140)) ||
            (fr != null && (fr < 12 || fr > 20)) ||
            (pulse != null && (pulse < 50 || pulse > 100)) ||
            (temp != null && (temp < 35.8 || temp > 38.5))
}

fun submitCare(
    patientId: Int?,
    ta: Int?, fr: Int?, pulse: Int?, temp: Double?,
    remoteViewModel: RemoteViewModel,
    nurseViewModel: NurseViewModel,
    onError: (String) -> Unit
) {
    if (patientId != null) {
        val newCare = CareState(
            id = null,
            ta_sistolica = ta,
            freq_resp = fr,
            pols = pulse,
            temperatura = temp,
            date = null
        )
        remoteViewModel.createCare(patientId, newCare, nurseViewModel)
    } else {
        onError("Falta de ID de pacient.")
    }
}


@Composable
fun CareInputField(
    valueState: MutableState<String>,
    labelId: String,
    icon: ImageVector,
    placeholderText: String,
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
        placeholder = { Text(text = placeholderText) },
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