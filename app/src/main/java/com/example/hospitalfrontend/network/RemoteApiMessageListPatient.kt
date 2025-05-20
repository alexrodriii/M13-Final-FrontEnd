package com.example.hospitalfrontend.network
import com.example.hospitalfrontend.model.PatientState


sealed class RemoteApiMessageListPatient {
    object Loading : RemoteApiMessageListPatient()
    object Empty : RemoteApiMessageListPatient()
    data class Success(val patients: List<PatientState>) : RemoteApiMessageListPatient()
    data class Error(val message: String) : RemoteApiMessageListPatient()
}
