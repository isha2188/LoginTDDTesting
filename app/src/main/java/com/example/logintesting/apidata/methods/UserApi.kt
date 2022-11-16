package com.example.logintesting.apidata.methods

import com.example.logintesting.apidata.ApiClient
import com.example.logintesting.apidata.request.LoginRequest
import com.example.logintesting.apidata.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/api/authaccount/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<LoginResponse>

    companion object {
        fun getApi(): UserApi? {
            return ApiClient.client?.create(UserApi::class.java)
        }
    }
}