package com.example.hospitalfrontend.network

import com.example.hospitalfrontend.model.NurseState
import com.example.hospitalfrontend.model.SearchState
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("nurse/{id}")
    suspend fun getNurseById(@Path("id") id: Int): NurseState

    @GET("nurse/name/{name}")
    suspend fun findByName(@Path("name") nurseName: String): NurseState
}