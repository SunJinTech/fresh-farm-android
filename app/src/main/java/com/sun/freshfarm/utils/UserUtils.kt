package com.sun.freshfarmadmin.utils

import android.util.Log
import com.google.android.gms.tasks.Task
import com.sun.freshfaram.utils.AuthUtils
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Profile
import com.sun.freshfarm.database.SaveDataCallBack
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.sun.freshfarm.Constants


class UserUtils() {
    private val db = Firebase.firestore

    fun profileRef(uid: String): DocumentReference {
        return db.collection("profiles").document(uid)
    }


    fun getProfile(uid: String): Task<Profile?> {
        return profileRef(uid).get().continueWith {
            if (it.result?.exists() == true) {
                return@continueWith it.result?.toObject<Profile>()
            } else {
                return@continueWith null
            }
        }
    }

    fun setProfile(profile: Profile): Task<String> {
        return profileRef(profile.uid).set(profile).continueWith {
            if (it.isSuccessful) {
                return@continueWith "프로필을 성공적으로 저장했습니다."
            } else {
                return@continueWith "프로필을 저장하는데 실패했습니다."
            }
        }
    }

    fun getProfile(uid: String, callBack: GetDataCallBack<Profile>) {
        profileRef(uid).get()
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    callBack.onCanceled("프로필 데이터를 읽어오는데 실패했습니다.")
                }
                val profile = it.result?.toObject<Profile>()
                if (profile != null) {
                    callBack.onDataReceived(profile)
                } else {
                    callBack.onCanceled("프로필 데이터가 존재하지 않습니다.")
                }
            }
    }

    fun saveToken(token: String, uid: String) {
        db.collection("tokens").document(uid).set(
            hashMapOf(
                "token" to token
            )
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("token", token)
            } else {
                Log.e("token", "damn")
            }
        }
    }

    fun saveProfile(profile: Profile, callBack: SaveDataCallBack) {
        profileRef(profile.uid).set(profile).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("data save tag", "data successfully saved : ${task.result}")
                callBack.onSuccess("result : ${task.result}")
            } else {
                callBack.onCanceled("error")
            }
        }
    }
}