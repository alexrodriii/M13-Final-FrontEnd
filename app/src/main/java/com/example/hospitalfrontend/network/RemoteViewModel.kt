package com.example.hospitalfrontend.network

import android.util.Log
import retrofit2.Retrofit
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hospitalfrontend.model.*
import retrofit2.converter.gson.GsonConverterFactory

class RemoteViewModel : ViewModel() {
    var remoteApiMessage = mutableStateOf<RemoteApiMessageNurse>(RemoteApiMessageNurse.Loading)
        private set
    var remoteApiListMessage =
        mutableStateOf<RemoteApiMessageListNurse>(RemoteApiMessageListNurse.Loading)

    fun clearApiMessage() {
        remoteApiMessage.value = RemoteApiMessageNurse.Loading
    }

    fun getNurseById(nurseId: Int) {
        viewModelScope.launch {
            remoteApiMessage.value = RemoteApiMessageNurse.Loading
            try {
                val connection = Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create()).build()
                val endPoint = connection.create(ApiService::class.java)
                // Call to the end-pont to find nurse by ID
                val response = endPoint.getNurseById(nurseId)
                remoteApiMessage.value = RemoteApiMessageNurse.Success(response)
            } catch (e: Exception) {
                remoteApiMessage.value = RemoteApiMessageNurse.Error
            }
        }
    }
    //We do the request to the API
    fun getAllNurses() {
        viewModelScope.launch {
            remoteApiListMessage.value = RemoteApiMessageListNurse.Loading
            try {
                val connection = Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create()).build()
                val endPoint = connection.create(ApiService::class.java)
                // Call to the end-pont to get all nurses
                val response = endPoint.getAll()
                remoteApiListMessage.value = RemoteApiMessageListNurse.Success(response)
            } catch (e: Exception) {
                remoteApiListMessage.value = RemoteApiMessageListNurse.Error
            }
        }
    }


    fun loginNurse(nurse: LoginRequest) {
        viewModelScope.launch {
            remoteApiMessage.value = RemoteApiMessageNurse.Loading
            try {
                val connection = Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create()).build()

                val endPoint = connection.create(ApiService::class.java)
                // Call to the end-point to login
                val response = endPoint.loginNurse(nurse)
                remoteApiMessage.value = RemoteApiMessageNurse.Success(response)
            } catch (e: Exception) {
                remoteApiMessage.value = RemoteApiMessageNurse.Error
            }
        }
    }

    fun createNurse(nurse: NurseState) {
        viewModelScope.launch {
            remoteApiMessage.value = RemoteApiMessageNurse.Loading
            try {
                val connection = Retrofit.Builder().baseUrl("http://10.0.2.2:8080/")
                    .addConverterFactory(GsonConverterFactory.create()).build()

                val endPoint = connection.create(ApiService::class.java)
                // Call to the end-point to create a new nurse
                val response = endPoint.createNurse(nurse)
                remoteApiMessage.value = RemoteApiMessageNurse.Success(response)
            } catch (e: Exception) {
                remoteApiMessage.value = RemoteApiMessageNurse.Error
            }
        }
    }

}