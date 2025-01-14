package com.example.hospitalfrontend.network

import com.example.hospitalfrontend.model.NurseState
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("nurse/{id}")
    suspend fun getNurseById(@Path("id") id: Int): NurseState
}