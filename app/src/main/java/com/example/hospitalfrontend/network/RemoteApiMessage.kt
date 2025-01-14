package com.example.hospitalfrontend.network

import com.example.hospitalfrontend.model.NurseState

sealed interface RemoteApiMessage {
    data class Success(val message: NurseState) : RemoteApiMessage
    object Loading : RemoteApiMessage
    object Error : RemoteApiMessage

}