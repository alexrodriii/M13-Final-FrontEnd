package com.example.hospitalfrontend.network

import com.example.hospitalfrontend.model.CareState

sealed interface RemoteApiMessageCare {
    object Idle : RemoteApiMessageCare
    object Loading : RemoteApiMessageCare
    data class Success(val care: CareState) : RemoteApiMessageCare
    data class Error(val errorMessage: String) : RemoteApiMessageCare
}