package com.example.hospitalfrontend.ui.nurses.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.hospitalfrontend.network.RemoteViewModel

@Composable
fun RoomScreen(remoteViewModel: RemoteViewModel = viewModel(), navController: NavController) {
    LaunchedEffect(Unit) {
        remoteViewModel.getAllRooms()
    }

    val rooms = remoteViewModel.rooms

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Botó Tancar"
                )
            }

            Text(
                text = "Habitacions",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )

            if (rooms.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(rooms) { room ->
                        Card(
                            onClick = {
                                navController.navigate("roomDetail/${room.id}")
                                Log.d("RoomScreen", "Room clicked: ${room.id}")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {

                                // Icono de hotel junto al texto de la habitación
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Hotel,
                                        contentDescription = "Icona Habitació",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Habitació: ${room.id}",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Observacions:",
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = room.observations ?: "No observations",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Text(
                                    text = "Pacients:",
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (room.patients.isNotEmpty()) {
                                    room.patients.forEach { patient ->
                                        Text(
                                            text = "- ${patient.name}",
                                            fontSize = 16.sp,
                                            modifier = Modifier.padding(
                                                start = 8.dp,
                                                top = 2.dp
                                            )
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "No hi ha pacients en aquesta habitació",
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(
                                            start = 8.dp,
                                            top = 4.dp
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
