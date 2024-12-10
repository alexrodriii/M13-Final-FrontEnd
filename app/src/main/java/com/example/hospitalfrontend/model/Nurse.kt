package com.example.hospitalfrontend.model

data class Nurse(
    val id: Int,
    val name: String,
    val surname: String,
    val age : Int,
    val email: String,
    val password: String,
    val speciality: String
)