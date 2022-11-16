package com.example.logintesting

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.logintesting.apidata.request.LoginRequest
import com.example.logintesting.apidata.response.BaseResponse
import com.example.logintesting.apidata.response.LoginResponse
import com.example.logintesting.repository.UserRepository
import kotlinx.coroutines.launch


class LoginViewModel: ViewModel() {

    var EmailAddress = MutableLiveData<String>()
    var Password = MutableLiveData<String>()
    val userRepo = UserRepository()
    val loginResult: MutableLiveData<BaseResponse<LoginResponse>> = MutableLiveData()


    private var userMutableLiveData: MutableLiveData<LoginUser>? = null

    fun getUser(): MutableLiveData<LoginUser>? {
        if (userMutableLiveData == null) {
            userMutableLiveData = MutableLiveData<LoginUser>()
        }
        return userMutableLiveData
    }

    fun onClick(view: View?) {
        val loginUser = LoginUser(EmailAddress.value, Password.value)
        userMutableLiveData!!.setValue(loginUser)
    }

    fun loginUser(email: String, pwd: String) {

        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                val loginRequest = LoginRequest(
                    password = pwd,
                    email = email
                )
                val response = userRepo.loginUser(loginRequest = loginRequest)
                if (response?.code() == 200) {
                    loginResult.value = BaseResponse.Success(response.body())
                } else {
                    loginResult.value = BaseResponse.Error(response?.message())
                }

            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }
}