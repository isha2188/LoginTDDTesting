package com.example.logintesting

import android.util.Patterns




class LoginUser(EmailAddress: String?, Password: String?) {

    private var strEmailAddress: String? = EmailAddress
    private var strPassword: String? = Password

    fun getStrEmailAddress(): String? {
        return strEmailAddress
    }

    fun getStrPassword(): String? {
        return strPassword
    }

    fun isEmailValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(getStrEmailAddress()).matches()
    }


    fun isPasswordLengthGreaterThan5(): Boolean {
        return getStrPassword()!!.length > 5
    }

}