package com.sun.freshfarm.user.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sun.freshfaram.utils.AuthUtils
import com.sun.freshfarm.R
import kotlinx.android.synthetic.main.activity_user_signup_email.*
import org.jetbrains.anko.*

class SignUpEmail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_signup_email)

        initializeView()

//        userSignUpEmailNextButton.setOnClickListener {
//            val email = userSignUpEmail.text.toString()
//            AuthUtils().emailValidation(email, object : AuthUtils.ActionCallBack {
//                override fun onSuccess(message: String) {
//                    startActivity(
//                        Intent(
//                            this@SignUpEmail,
//                            SignUpPassword::class.java
//                        ).putExtra("email", email)
//                    )
//                    finish()
//                }
//
//                override fun onFailure(message: String) {
//                    toast(message)
//                }
//
//            })
//        }
    }

    private fun initializeView() {
        userSignUpNext?.setOnClickListener {
            submitEmailAndName()
        }
    }

    private fun submitEmailAndName() {
        val name = userSignUpName.text.toString().replace(" ", "")
        val email = userSignUpEmail.text.toString().replace(" ", "")

        if (name.isEmpty()) {
            toast("이름을 입력해주세요.")
            return
        }

        when (AuthUtils().emailValidation(email)) {
            AuthUtils.EMAIL_CHECK_CODE.BLANK ->
                toast("이메일 주소를 입력해주세요.")
            AuthUtils.EMAIL_CHECK_CODE.FAIL ->
                toast("올바른 이메일 주소를 입력해주세요.")
            AuthUtils.EMAIL_CHECK_CODE.OK ->
                moveNext(name, email)
        }
    }

    private fun moveNext(name: String, email: String) {
        startActivity(
            Intent(
                this@SignUpEmail,
                SignUpPassword::class.java
            ).putExtra("email", email).putExtra("name", name)
        )
        finish()
    }
}
