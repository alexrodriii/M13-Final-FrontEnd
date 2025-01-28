package com.example.hospitalfrontend.ui.nurses.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospitalfrontend.R
import com.example.hospitalfrontend.model.NurseState
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel
import com.example.hospitalfrontend.ui.theme.*
import androidx.compose.runtime.collectAsState
import com.example.hospitalfrontend.network.RemoteApiMessageNurse
import com.example.hospitalfrontend.network.RemoteViewModel


@Preview

@Composable
fun MySearchPreview() {
    HospitalFrontEndTheme {
        val navController = rememberNavController()
        val nurseViewModel = NurseViewModel()
        val remoteViewModel = RemoteViewModel()
        FindScreen(
            navController,
            remoteViewModel,
            nurseViewModel,
        )
    }

}

@Composable
fun TextField(labelValue: String, onValueChange: (String) -> Unit, textFieldValue: String = "") {
    OutlinedTextField(
        label = { Text(text = labelValue) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Primary,
            cursorColor = Primary,
            focusedLabelColor = Primary,
        ),
        keyboardOptions = KeyboardOptions.Default,
        value = textFieldValue,
        onValueChange = { onValueChange(it) }
    )
}

@Composable
fun ListSearchNurse(nurse: NurseState) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.foundation.Image(
                painter = painterResource(id = R.drawable.nurse_profile),
                contentDescription = "Image Profile",
                modifier = Modifier.size(50.dp)
            )
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(text = "Name: ${nurse.name}")
                Text(text = "Surname: ${nurse.surname}")
                Text(text = "Email: ${nurse.email}")
                Text(text = "Speciality: ${nurse.speciality}")
            }
        }
    }
}

@Composable
fun FindScreen(
    navController: NavController,
    remoteApiMessage: RemoteViewModel,
    nurseViewModel: NurseViewModel
) {
    val currentSearchName by nurseViewModel.currentSearchName.collectAsState()
    val message = remoteApiMessage.remoteApiMessage.value

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {
            // Botón de cierre
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close Button",
                    tint = colorResource(id = R.color.colorText)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para el nombre
            TextField(
                labelValue = "Name of nurse",
                onValueChange = {
                    nurseViewModel.updateCurrentSearchName(it)
                },
                textFieldValue = currentSearchName
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Botón para buscar
            ButtonComponent(
                value = "Search",
                enabled = currentSearchName.isNotBlank()
            ) {
                remoteApiMessage.findByName(currentSearchName)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Manejo de estados de UI
            when (message) {
                is RemoteApiMessageNurse.Loading -> {
                    Log.d("Loading", "Searching Nurse")
                }

                is RemoteApiMessageNurse.Error -> {
                    Text(
                        text = "Error fetching nurse",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                is RemoteApiMessageNurse.Success -> {
                    ListSearchNurse(message.message)
                }
            }
        }
    }
}

