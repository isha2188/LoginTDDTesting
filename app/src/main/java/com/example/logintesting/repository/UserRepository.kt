package com.example.logintesting.repository


import com.example.logintesting.apidata.methods.UserApi
import com.example.logintesting.apidata.request.LoginRequest
import com.example.logintesting.apidata.response.LoginResponse
import retrofit2.Response

class UserRepository {

   suspend fun loginUser(loginRequest: LoginRequest): Response<LoginResponse>? {
      return  UserApi.getApi()?.loginUser(loginRequest = loginRequest)
    }
}