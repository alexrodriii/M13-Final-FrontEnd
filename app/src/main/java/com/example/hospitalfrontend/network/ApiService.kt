package com.example.hospitalfrontend.network

import com.example.hospitalfrontend.model.CareState
import com.example.hospitalfrontend.model.Diagnosis
import com.example.hospitalfrontend.model.LoginRequest
import com.example.hospitalfrontend.model.NurseState
import com.example.hospitalfrontend.model.PatientState
import com.example.hospitalfrontend.model.Room
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @GET("nurse/{id}")
    suspend fun getNurseById(@Path("id") id: Int): NurseState

    @GET("nurse")
    suspend fun getAll(): List<NurseState>

    @GET("/nurse/patient/{id}/diagnoses")
    suspend fun getDiagnosis(@Path("id") id: Int): List<Diagnosis>

    @GET("/nurse/patient/{id}")
    suspend fun getPatients(@Path("id") id: Int): NurseState

    @GET("nurse/room/{roomId}/patients")
    suspend fun getPatientsByRoom(@Path("roomId") roomId: String): List<PatientState>

    @GET("nurse/care/{id}")
    suspend fun getCarebyPatient(@Path("id") patientId: Int): List<CareState>

    @POST("nurse/login")
    suspend fun loginNurse(@Body loginRequest: LoginRequest): NurseState

    @POST("nurse")
    suspend fun createNurse(@Body nurse: NurseState): NurseState

    @DELETE("nurse/{id}")
    suspend fun deleteNurse(@Path("id") id: Int): Boolean

    @GET("nurse/name/{name}")
    suspend fun findByName(@Path("name") nurseName: String): NurseState

    @PUT("nurse/{id}")
    suspend fun updateNurse(@Path("id") id: Int, @Body updateNurse: NurseState): NurseState

    @GET("nurse/photo/{id}")
    suspend fun getPhotoById(@Path("id") id: Int): Response<ResponseBody>

    @GET("nurse/allRoom")
    suspend fun getAllRooms(): List<Room>
    @POST("nurse/care/{patientId}/{nurseId}")
    suspend fun createCare(@Path("patientId") patientId: Int,@Path("nurseId") nurseId: Int,@Body care: CareState): CareState
    @GET("nurse/care/detail/{id}")
    suspend fun getCareById(@Path("id") careId: Int): CareState


    @Multipart
    @POST("nurse/photo/{id}")
    suspend fun uploadPhotoById(
        @Path("id") id: Int,
        @Part file: MultipartBody.Part
    ): Response<ResponseBody>
}