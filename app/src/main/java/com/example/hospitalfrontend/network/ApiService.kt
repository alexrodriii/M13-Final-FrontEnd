package com.example.hospitalfrontend.network

import com.example.hospitalfrontend.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("nurse/{id}")
    suspend fun getNurseById(@Path("id") id: Int): NurseState

    @GET("nurse")
    suspend fun getAll(): List<NurseState>

    @POST("nurse/login")
    suspend fun loginNurse(@Body loginRequest: LoginRequest): NurseState

    @POST("nurse")
    suspend fun createNurse(@Body nurse: NurseState): NurseState
}