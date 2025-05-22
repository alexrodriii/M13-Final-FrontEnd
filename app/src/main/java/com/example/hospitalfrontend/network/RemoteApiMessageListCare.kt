package com.example.hospitalfrontend.network
import com.example.hospitalfrontend.model.CareState
import com.example.hospitalfrontend.model.PatientState

interface RemoteApiMessageListCare {

    object Idle : RemoteApiMessageListCare
    object Loading : RemoteApiMessageListCare
    data class Success(val care: List<CareState>) : RemoteApiMessageListCare
    data class Error(val errorMessage: String) : RemoteApiMessageListCare

    data class CareListResponse(val cares: List<CareState>)
}