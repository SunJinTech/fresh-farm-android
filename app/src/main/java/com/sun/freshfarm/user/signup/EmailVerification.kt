package com.sun.freshfarm.user.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.freshfaram.utils.AuthUtils
import com.sun.freshfarm.home.HomeActivity
import com.sun.freshfarm.InitialActivity
import com.sun.freshfarm.Navigator
import com.sun.freshfarm.R
import kotlinx.android.synthetic.main.activity_user_signup_email_validation.*
import org.jetbrains.anko.toast

class EmailVerification : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_signup_email_validation)

        if (AuthUtils().user == null) {
            toast("로그인 해주세요.")
            finish()
        }

        initialView()
    }

    override fun onStart() {
        super.onStart()
        if (!AuthUtils().isLoggedIn) {
            Navigator(this).goLoginPage()
        }
    }


    private fun initialView() {
        val name = AuthUtils().user?.displayName
        val email = AuthUtils().user?.email

        emailVerificationInformText.text = "${name}님, ${email}로 인증 메일이 전송되었습니다."


        emailVerificationSignOut.setOnClickListener {
            AuthUtils().doSignOut(object : AuthUtils.ActionCallBack {
                override fun onSuccess(message: String) {
                    toast(message)
                    startActivity(Intent(this@EmailVerification, InitialActivity::class.java))
                    finish()
                }

                override fun onFailure(message: String) {
                    toast(message)
                }
            })
        }

        emailVerificationConfirm.setOnClickListener {
            checkVerification()
        }

        emailVerificationResend.setOnClickListener {
            tryResendVerification()
        }
    }

    private fun checkVerification() {
        AuthUtils().checkEmailVerification(object : AuthUtils.ActionCallBack {
            override fun onSuccess(message: String) {
                toast(message)
                startActivity(Intent(this@EmailVerification, HomeActivity::class.java))
                finish()
            }

            override fun onFailure(message: String) {
                toast(message)
            }
        })
    }

    private fun tryResendVerification() {
        AuthUtils().sendEmailVerification (object : AuthUtils.ActionCallBack {
            override fun onSuccess(message: String) {
                toast(message)
            }

            override fun onFailure(message: String) {
                toast(message)
            }
        })
    }
}