package com.app.data.network.services

import com.app.data.network.entities.Login
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET

interface AuthorizationNetworkService {

    @GET("login/")
    fun listRepos(@Body loginData: Login): Call<List<String?>?>?
}