package com.example.hospitalfrontend.model

data class PatientState(
    val id: Int,
    val name : String,
    val dni : Int,
    val telefono: Int,
    val direccion : String,
    val correo : String,
    val historyNumber: Int?

)
