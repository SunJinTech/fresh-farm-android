package com.sun.freshfarm.user.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.freshfaram.utils.AuthUtils
import com.sun.freshfarm.InitialActivity
import com.sun.freshfarm.R
import com.sun.freshfarm.user.signup.SignUpEmail
import kotlinx.android.synthetic.main.activity_user_login.*
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    private var loading = false

    private val fragmentManager = supportFragmentManager
//    private val loadingFragment = LoadingFragmen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login)

        if (AuthUtils().isLoggedIn) {
            toast("이미 로그인 되어있는 유저입니다.")
            finish()
        }

        userLoginButton.setOnClickListener {
            doLogin()
        }

        userLoginSignUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpEmail::class.java))
        }
    }


    private fun doLogin() {
        if (loading) {
            return
        } else {
            loading = true
        }

        val email = userLoginEmailInput.text.toString()
        val password = userLoginPassInput.text.toString()

//        setContentView(R.layout.loading_page)
//        loadingMessage.text = "Please wait for login"

        AuthUtils().doLogin(email, password, object : AuthUtils.ActionCallBack {
            override fun onSuccess(message: String) {
                startActivity(Intent(this@LoginActivity, InitialActivity::class.java))
                finish()
            }

            override fun onFailure(message: String) {
                toast(message)
                loading = false
            }
        })
    }
}
