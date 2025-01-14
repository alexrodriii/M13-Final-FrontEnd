package com.example.hospitalfrontend.network

import retrofit2.Retrofit
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import retrofit2.converter.gson.GsonConverterFactory

class RemoteViewModel : ViewModel() {
    private var remoteApiMessage: RemoteApiMessage by mutableStateOf(RemoteApiMessage.Loading)

    fun getNurseById(nurseId: Int) {
        viewModelScope.launch {
            remoteApiMessage = RemoteApiMessage.Loading
            try {
                val connection = Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create()).build()
                val endPoint = connection.create(ApiService::class.java)
                val response = endPoint.getNurseById(nurseId)
                remoteApiMessage = RemoteApiMessage.Success(response)
            } catch (e: Exception) {
                remoteApiMessage = RemoteApiMessage.Error
            }
        }
    }

}