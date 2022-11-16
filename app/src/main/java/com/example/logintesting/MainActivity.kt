package com.example.logintesting

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.logintesting.apidata.response.BaseResponse
import com.example.logintesting.apidata.response.LoginResponse
import com.example.logintesting.databinding.ActivityMainBinding
import com.example.logintesting.utils.SessionManager
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var databinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginViewModel =  ViewModelProvider(this).get(LoginViewModel::class.java)
        databinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main);
        databinding.loginViewModel = loginViewModel
        databinding.lifecycleOwner = this
        databinding.txtEmailAddress.requestFocus()
        val token = SessionManager.getToken(this)
        if (!token.isNullOrBlank()) {
            navigateToHome()
        }
        loginViewModel.getUser()?.observe(this, object: Observer<LoginUser> {
            override fun onChanged(loginUser: LoginUser?) {
                if (TextUtils.isEmpty(Objects.requireNonNull(loginUser)?.getStrEmailAddress())) {
                    databinding.txtEmailAddress.setError("Enter an E-Mail Address");
                    databinding.txtEmailAddress.requestFocus();
                }
                else if (!loginUser?.isEmailValid()!!) {
                    databinding.txtEmailAddress.setError("Enter a Valid E-mail Address");
                    databinding.txtEmailAddress.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser)?.getStrPassword())) {
                    databinding.txtPassword.setError("Enter a Password");
                    databinding.txtPassword.requestFocus();
                }
                else if (!loginUser?.isPasswordLengthGreaterThan5()!!) {
                    databinding.txtPassword.setError("Enter at least 6 Digit password");
                    databinding.txtPassword.requestFocus();
                }
                else {
                    databinding.lblEmailAnswer.setText(loginUser?.getStrEmailAddress());
                    databinding.lblPasswordAnswer.setText(loginUser?.getStrPassword());
                    loginViewModel.loginUser(email = loginUser?.getStrEmailAddress()!!, pwd = loginUser?.getStrPassword()!!)
                }

            }

            })
        loginViewModel.loginResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()
                    processLogin(it.data)
                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                else -> {
                    stopLoading()
                }
            }
        }

        }
    private fun navigateToHome() {
        val intent = Intent(this, LogoutActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    fun showLoading() {
        databinding.prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        databinding.prgbar.visibility = View.GONE
    }

    fun processLogin(data: LoginResponse?) {
        showToast("Success:" + data?.message)
        if (!data?.data?.token.isNullOrEmpty()) {
            data?.data?.token?.let { SessionManager.saveAuthToken(this, it) }
            navigateToHome()
        }
    }

    fun processError(msg: String?) {
        showToast("Error:" + msg)
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }



}