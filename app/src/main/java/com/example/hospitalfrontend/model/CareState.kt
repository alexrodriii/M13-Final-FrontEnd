// M13-Final-FrontEnd/app/src/main/java/com/example/hospitalfrontend/model/CareState.kt
package com.example.hospitalfrontend.model

import java.time.LocalDate

data class CareState(
    val id: Int?, // Id del cuidado. Anulable para robustez
    val ta_sistolica: Int?, // Descripción del cuidado
    val freq_resp: Int?, // Descripción del cuidado
    val pols: Int?, // Descripción del cuidado
    val temperatura: Int?, // Descripción del cuidado

    )