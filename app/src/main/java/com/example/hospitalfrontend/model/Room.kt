package com.example.hospitalfrontend.model

data class Room(
    val id: String,
    val observations: String,
    val patients: List<PatientState>,
    val number: Int

)