package com.example.hospitalfrontend.network

import com.example.hospitalfrontend.model.PatientState

sealed interface RemoteApiMessageListPatient {
    object Loading : RemoteApiMessageListPatient
    data class Success(val data: List<PatientState>) : RemoteApiMessageListPatient
    object Error : RemoteApiMessageListPatient
}

