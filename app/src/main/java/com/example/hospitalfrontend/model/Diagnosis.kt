package com.example.hospitalfrontend.model

import com.google.gson.annotations.SerializedName

data class Diagnosis(
    val id: Int,
    val diagnostico: String,
    val motivo: String,

    @SerializedName("portadorO2Tipus")
    val portadorO2Tipus: String?,

    @SerializedName("portadorBolquer")
    val portadorBolquer: Boolean?,

    @SerializedName("numeroCanvisBolquer")
    val numeroCanvisBolquer: Int?,

    @SerializedName("estatPell")
    val estatPell: String?
)
