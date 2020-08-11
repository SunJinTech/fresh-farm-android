package com.sun.freshfarm.user.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sun.freshfarm.InitialActivity
import com.sun.freshfarm.R
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Profile
import com.sun.freshfarmadmin.utils.UserUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sun.freshfaram.utils.AuthUtils
import com.sun.freshfarm.Navigator
import kotlinx.android.synthetic.main.activity_user_profile.*
import org.jetbrains.anko.toast

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        profileSignOutTextButton.setOnClickListener {
            AuthUtils().doSignOut(object : AuthUtils.ActionCallBack {
                override fun onSuccess(message: String) {
                    Navigator(this@ProfileActivity).doInitial()
                }

                override fun onFailure(message: String) {
                    toast(message)
                }
            })
        }

        profileGoEditProfileTextButton.setOnClickListener {
            if (AuthUtils().isLoggedIn) {
                Navigator(this).startProfileEdit(AuthUtils().uid!!)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!AuthUtils().isLoggedIn) {
            Navigator(this).goLoginPage()
        }

        UserUtils().getProfile(AuthUtils().uid!!, object : GetDataCallBack<Profile> {
            override fun onDataReceived(data: Profile) {
                updateUI(data)
            }

            override fun onCanceled(message: String) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun updateUI(profile: Profile) {
        profileNameText.text = profile.name
        profileEmailText.text = profile.email
        profileFarmNameText.text = profile.farmName
        profileFarmLocationText.text = profile.farmLocation
        profileFarmIntroduceText.text = profile.introduce
    }
}
