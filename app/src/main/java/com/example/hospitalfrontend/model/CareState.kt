// M13-Final-FrontEnd/app/src/main/java/com/example/hospitalfrontend/model/CareState.kt
package com.example.hospitalfrontend.model

import java.time.*
import java.util.Date

data class CareState(
    val id: Int?,
    val ta_Sistolica: Int?,
    val ta_Distolica: Int?,
    val freq_resp: Int?,
    val pols: Int?,
    val temperatura: Int?,
    val date: Date? = null,
    val nurse: NurseState? = null

    )