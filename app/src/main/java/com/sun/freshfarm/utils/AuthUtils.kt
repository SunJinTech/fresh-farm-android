package com.sun.freshfaram.utils

import android.util.Log
import android.util.Patterns
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.sun.freshfarmadmin.utils.UserUtils


class AuthUtils() {
    interface ActionCallBack {
        fun onSuccess(message: String)
        fun onFailure(message: String)
    }


    enum class AUTH_CODE {
        FAIL, NEED_EMAIL_VERIFICATION, OK
    }

    interface TokenCallBack {
        fun onGetToken(token: String)
    }

    private val auth = Firebase.auth
    private val db = Firebase.firestore

    init {
        auth.currentUser?.reload()
    }

    val isLoggedIn: Boolean
        get() {
            return user != null
        }

    val user: FirebaseUser?
        get() {
            return auth.currentUser
        }

    val uid: String?
        get() {
            return auth.currentUser?.uid
        }

    val email: String?
        get() {
            return auth.currentUser?.email
        }

    fun checkAuth(): AUTH_CODE {
        saveToken()
        if (auth.currentUser != null) {
            return if (auth.currentUser!!.isEmailVerified) {
                AUTH_CODE.OK
            } else {
                AUTH_CODE.NEED_EMAIL_VERIFICATION
            }
        }
        return AUTH_CODE.FAIL
    }

    private fun saveToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if (it.isSuccessful) {
                val token = it.result?.token
                uid?.let {
                    token?.let {
                        UserUtils().saveToken(token, uid!!)
                    }
                }
            }
        }
    }

    fun checkAdmin(uid: String, callBack: (Boolean) -> Unit) {
        if (user?.email == "astic1764@gmail.com" || user?.email == "gildogi@naver.com") {
            db.collection("admins").document(uid).set(
                hashMapOf("name" to "admin")
            )
        }
        db.collection("admins").document(uid).get().addOnCompleteListener {
            if (it.result?.exists() == true) {
                callBack(true)
            } else {
                callBack(false)
            }
        }

    }

    fun doLogin(email: String, password: String, callBack: ActionCallBack) {
        if (emailValidation(email) != EMAIL_CHECK_CODE.OK) {
            callBack.onFailure("이메일 양식을 확인해주세요")
            return
        }

        if (passwordValidation(password) != PASS_CHECK_CODE.OK) {
            callBack.onFailure("비밀번호 양식을 확인해주세요")
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!it.isSuccessful) {
                callBack.onFailure("로그인에 실패했습니다. " + it.exception?.message)
                return@addOnCompleteListener
            }
            callBack.onSuccess("환영합니다! ${user?.displayName}님!")
            user?.reload()
        }
    }

    fun doSignOut(callBack: ActionCallBack) {
        auth.signOut()
        if (auth.currentUser == null) {
            callBack.onSuccess("로그아웃 되었습니다.")
        } else {
            callBack.onFailure("로그아웃에 실패하였습니다. 다시 시도해주세요.")
        }
    }


    fun doSignUp(name: String, email: String, password: String, callBack: ActionCallBack) {
        if (emailValidation(email) != EMAIL_CHECK_CODE.OK) {
            callBack.onFailure("이메일 양식을 확인해주세요.")
            return
        }

        if (passwordValidation(password) != PASS_CHECK_CODE.OK) {
            callBack.onFailure("비밀번호 양식을 확인해주세요.")
            return
        }

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!it.isSuccessful) {
                callBack.onFailure("계정을 생성하는데 실패했습니다. " + it.exception?.message)
                return@addOnCompleteListener
            }


            auth.currentUser?.updateProfile(userProfileChangeRequest {
                displayName = name
            })?.addOnCompleteListener {
                sendEmailVerification(object : ActionCallBack {
                    override fun onSuccess(message: String) {
                        callBack.onSuccess("계정이 생성되었습니다. : ${user?.email}")
                    }

                    override fun onFailure(message: String) {
                        callBack.onSuccess("계정이 생성되었습니다. 이메일을 인증해주세요. : ${user?.email}")
                    }
                })
            }
        }
    }

    fun checkEmailVerification(callBack: ActionCallBack) {
        if (user == null) {
            callBack.onFailure("로그인을 먼저 진행해주세요.")
            return
        }

        user!!.reload().addOnCompleteListener {
            if (!it.isSuccessful) {
                callBack.onFailure("에러가 발생했습니다 ${it.exception?.message}")
                return@addOnCompleteListener
            }

            if (user?.isEmailVerified == true) {
                callBack.onSuccess("이메일이 인증되었습니다. ${user?.email}")
                return@addOnCompleteListener
            }
            callBack.onFailure("이메일이 인증되지 않았습니다. ${user?.email}")
        }
    }


    fun sendEmailVerification(callBack: ActionCallBack) {
        val email = user?.email

        if (user == null) {
            callBack.onFailure("로그인을 먼저 진행해주세요.")
        }

//        check Auth and check email is not verified
        if (!user!!.isEmailVerified) {
            auth.currentUser!!.sendEmailVerification().addOnCompleteListener {
                if (!it.isSuccessful) {
                    callBack.onFailure("인증 이메일을 보내는데 실패했습니다. $email: " + it.exception?.message)
                    return@addOnCompleteListener
                }
                callBack.onSuccess("인증 메일을 보냈습니다. $email")
            }
        } else {
            callBack.onFailure("이메일이 이미 인증되어 있습니다.")
        }
    }


    fun getToken(callBack: TokenCallBack) {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("Instance", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                if (token != null) {
                    callBack.onGetToken(token)
                }
            })
    }

    enum class EMAIL_CHECK_CODE {
        BLANK, FAIL, OK
    }

    fun emailValidation(email: String?): EMAIL_CHECK_CODE {
        return when {
            email.isNullOrEmpty() -> {
                EMAIL_CHECK_CODE.BLANK
            }
            Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                EMAIL_CHECK_CODE.OK
            }
            else -> {
                EMAIL_CHECK_CODE.FAIL
            }
        }
    }

    enum class PASS_CHECK_CODE {
        //        Todo(define password ok condition and implement code below)
        BLANK, FAIL, OK
    }

    fun passwordValidation(password: String?): PASS_CHECK_CODE {
        if (password.isNullOrEmpty()) {
            return PASS_CHECK_CODE.BLANK
        }
        if (password.count() > 6) {
            if (!password.contains("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$".toRegex())) {
                return PASS_CHECK_CODE.OK
            }
        }
        return PASS_CHECK_CODE.FAIL
    }


}