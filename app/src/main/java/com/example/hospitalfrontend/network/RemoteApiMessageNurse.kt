package com.example.hospitalfrontend.network

import com.example.hospitalfrontend.model.NurseState

sealed interface RemoteApiMessageNurse {
    data class Success(val message: NurseState) : RemoteApiMessageNurse
    object Loading : RemoteApiMessageNurse
    object Error : RemoteApiMessageNurse
}