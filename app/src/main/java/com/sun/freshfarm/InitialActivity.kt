package com.sun.freshfarm

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sun.freshfaram.utils.AuthUtils
import com.sun.freshfarm.utils.LivestockUtils
import com.sun.freshfarmadmin.utils.UserUtils


class InitialActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_page)
    }


    override fun onStart() {
        super.onStart()
        initialApp()
    }

    //    initial app => check permission -> check login => check profile => getLivestocks => home activity
    private fun initialApp() {
        checkPermission()
    }


    private fun checkPermission() {
        val permissions = arrayListOf<String>()

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissions.toTypedArray(),
                1
            )
        } else {
            checkLogin()
        }
    }

    private fun checkLogin() {
        when (AuthUtils().checkAuth()) {
            AuthUtils.AUTH_CODE.OK -> {
                AuthUtils().checkAdmin(AuthUtils().uid!!) {
                    Constants.isAdmin = it
                    checkProfile(AuthUtils().uid!!)
                }
            }
            AuthUtils.AUTH_CODE.NEED_EMAIL_VERIFICATION ->
                verifyEmail()

            AuthUtils.AUTH_CODE.FAIL ->
                requireLogin()
        }

    }


    private fun checkProfile(uid: String) {
        UserUtils().getProfile(uid).addOnCompleteListener {
            if (it.result != null) {
                Constants.myProfile = it.result
                checkLivestock(uid)
            } else {
                startProfileEdit(uid)
            }
        }
    }


    private fun checkLivestock(uid: String) {
        LivestockUtils().getLivestock(uid).addOnCompleteListener {
            val currentList = arrayListOf<String>()
            it.result?.map { livestock ->
                if (livestock.count > 0) {
                    currentList.add(livestock.kind)
                    goHome()
                }
            }
            Constants.livestocks = currentList
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() && grantResults.filter {
                        it != PackageManager.PERMISSION_GRANTED
                    }.size == grantResults.size)
                ) {
                    // 권한 허용됨
                    checkLogin()
                } else {
                    // 권한 거부
                    checkLogin()
                }
                return
            }
        }
    }

    private fun startProfileEdit(uid: String) {
        Navigator(this).goProfileEdit(uid)
    }

    private fun goHome() {
        Navigator(this).goHomePage()
        finish()
    }

    private fun verifyEmail() {
        Navigator(this).goEmailVerificationPage()
        finish()
    }

    private fun requireLogin() {
        Navigator(this).goLoginPage()
        finish()
    }
}
