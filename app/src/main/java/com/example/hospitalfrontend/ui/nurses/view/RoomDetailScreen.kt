package com.example.hospitalfrontend.ui.nurses.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hospitalfrontend.network.RemoteViewModel

@Composable
fun RoomDetailScreen(remoteViewModel: RemoteViewModel = viewModel(), navController: NavController, roomId: String?) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp)
                .size(24.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close Button"
                )
            }

            Text(
                "Room Detail",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (roomId != null) {
                Text(
                    text = "Selected Room ID: $roomId",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Text(
                    text = "No Room Selected",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}