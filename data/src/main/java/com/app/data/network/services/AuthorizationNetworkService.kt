package com.app.data.network.services

import com.app.data.network.entities.Login
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthorizationNetworkService {

    @POST("login/")
    suspend fun login(@Body loginData: Login): Call<List<String?>?>?

    @POST("auth/confirm_phone_number")
    suspend fun getConfirmationNumber(@Query("phone_number") phoneNumber: String): Response<String>

    @GET("test")
    suspend fun checkAuth(): Response<Any>
}