package com.sun.freshfarm.user.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.freshfaram.utils.AuthUtils
import com.sun.freshfarm.Navigator
import com.sun.freshfarm.R
import kotlinx.android.synthetic.main.activity_user_signup_password.*
import org.jetbrains.anko.toast

class SignUpPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_signup_password)

        val name = intent.extras?.getString("name")
        val email = intent.extras?.getString("email")

        if (name.isNullOrEmpty() || email.isNullOrEmpty()) {
            toast("사용자 정보를 읽어오는데 실패했습니다.")
            startActivity(Intent(this, SignUpEmail::class.java))
            finish()
        }

        initialView(name!!, email!!)
    }

    private fun initialView(name: String, email: String) {
        userSignUpPassWelcome?.text = "어서오세요, ${name}님. \n ${email}"

        userSignUpPassSubmit.setOnClickListener {
            val password = userSignUpPassInput.text.toString()
            val check = userSignUpPassCheckInput.text.toString()

            if (password != check) {
                toast("입력해주신 비밀번호와 확인란이 같지 않습니다.")
            }

            when (AuthUtils().passwordValidation(password)) {
                AuthUtils.PASS_CHECK_CODE.OK ->
                    Navigator(this).goCreatingAccountPage(
                        name,
                        email,
                        password
                    )
                AuthUtils.PASS_CHECK_CODE.BLANK ->
                    toast("비밀번호를 입력해주세요.")
                AuthUtils.PASS_CHECK_CODE.FAIL ->
                    toast("비밀번호가 양식에 맞지 않습니다.")
            }
        }
    }
}
