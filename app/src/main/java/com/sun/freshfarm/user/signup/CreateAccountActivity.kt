package com.sun.freshfarm.user.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.freshfaram.utils.AuthUtils
import com.sun.freshfarm.Navigator
import com.sun.freshfarm.R
import org.jetbrains.anko.toast

class CreateAccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        val name = intent.extras?.getString("name")
        val email = intent.extras?.getString("email")
        val password = intent.extras?.getString("password")

        if (
            name.isNullOrEmpty() ||
            email.isNullOrEmpty() ||
            password.isNullOrEmpty()
        ) {
            toast("입력해주신 정보를 읽어오지 못했습니다. 다시 시도해주세요.")
            Navigator(this).goSignUpPage()
        }

        createAccount(name!!, email!!, password!!)
    }

    private fun createAccount(name: String, email: String, password: String) {
        AuthUtils().doSignUp(name, email, password, SignUpCallBack())
    }

    private open inner class SignUpCallBack() : AuthUtils.ActionCallBack {
        override fun onSuccess(message: String) {
            toast(message)
            Navigator(this@CreateAccountActivity).doInitial()
        }

        override fun onFailure(message: String) {
            toast(message)
            Navigator(this@CreateAccountActivity).doInitial()
        }
    }
}