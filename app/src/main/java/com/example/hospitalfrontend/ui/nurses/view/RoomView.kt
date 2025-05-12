package com.example.hospitalfrontend.ui.nurses.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.material3.IconButton
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.hospitalfrontend.network.RemoteViewModel
import kotlinx.coroutines.launch

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Icon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme

@Composable
fun RoomScreen(remoteViewModel: RemoteViewModel = viewModel(), navController: NavController) {
    LaunchedEffect(Unit) {
        remoteViewModel.getAllRooms()
    }
    val rooms = remoteViewModel.rooms
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
                    "Rooms",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.headlineLarge
                )
                LazyColumn {
                    if (rooms.isEmpty()) {
                        item {
                            CircularProgressIndicator()
                        }
                    } else {
                        items(rooms) { room ->
                            Button(
                                onClick = {
                                    navController.navigate("roomDetail/${room.id}") {
                                    Log.d("RoomScreen", "NavController: $navController")
                                        println("Room clicked: ${room.observations} (ID: ${room.id})")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                            ) {
                                Text(text = room.observations, fontSize = 20.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}