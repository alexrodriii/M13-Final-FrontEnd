package com.example.hospitalfrontend.ui.nurses.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import com.example.hospitalfrontend.network.RemoteApiMessage
import com.example.hospitalfrontend.ui.nurses.viewmodels.NurseViewModel
import com.example.hospitalfrontend.ui.theme.*
import androidx.compose.runtime.collectAsState
import com.example.hospitalfrontend.network.RemoteViewModel


@Preview

@Composable
fun MySearchPreview() {
    HospitalFrontEndTheme {
        val navController = rememberNavController()
        FindScreen(navController, nurseViewModel = NurseViewModel(), remoteApiMessage = RemoteViewModel)
    }

}

@Composable
fun TextField(labelValue: String, viewModel: NurseViewModel) {
    val searchState by viewModel.searchState.collectAsState()

    OutlinedTextField(label = { Text(text = labelValue) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Primary,
            cursorColor = Primary,
            focusedLabelColor = Primary,
        ),
        keyboardOptions = KeyboardOptions.Default,
        value = searchState.nurseName,
        onValueChange = { viewModel.updateSearchName(it) })
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
    navController: NavController, nurseViewModel: NurseViewModel,
    remoteApiMessage: RemoteViewModel
) {
    val searchState by nurseViewModel.searchState.collectAsState()
    val message = remoteApiMessage.remoteApiMessage

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
        ) {
            // Back button at the top-right
            IconButton(
                onClick = {
                    navController.popBackStack()
                }, modifier = Modifier
                    .align(Alignment.TopEnd) // Position at top-right
                    .zIndex(1f) // Ensures this is above LazyColumn
            ) {
                Icon(
                    imageVector = Icons.Filled.Close, // Example icon
                    contentDescription = "Close Button",
                    tint = colorResource(id = R.color.colorText)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField("Name of nurse", nurseViewModel)
            Spacer(modifier = Modifier.height(25.dp))

            ButtonComponent(
                value = "Search",
                enabled = searchState.nurseName.isNotBlank()
            ) {
                nurseViewModel.findNurseByName()
            }
            Spacer(modifier = Modifier.height(20.dp))

            LaunchedEffect(message) {
                when (message) {
                    is RemoteApiMessage.Loading -> Log.d("Test", "Load")
                    is RemoteApiMessage.Error -> Log.d("Test","ERROR")
                    is RemoteApiMessage.Success -> {

                    }

                    else -> {}
                }
            }


        }
    }
}
