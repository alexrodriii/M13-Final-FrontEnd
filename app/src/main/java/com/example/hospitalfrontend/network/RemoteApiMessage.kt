package com.example.hospitalfrontend.network

import com.example.hospitalfrontend.model.NurseState
import kotlinx.coroutines.CoroutineScope

sealed interface RemoteApiMessage : suspend CoroutineScope.() -> Unit {
    data class Success(val message: NurseState) : RemoteApiMessage
    object Loading : RemoteApiMessage
    object Error : RemoteApiMessage

}