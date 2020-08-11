package com.sun.freshfarm.utils

import com.google.android.gms.tasks.Task
import com.sun.freshfaram.utils.AuthUtils
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Livestock
import com.sun.freshfarm.database.SaveDataCallBack
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class LivestockUtils {
    private val db = Firebase.firestore

//    private fun livestockRef(): DocumentReference {
//        return db.collection("livestocks").document(AuthUtils().uid)
//    }

    private fun livestockRef(uid: String): DocumentReference {
        return db.collection("livestocks").document(uid)
    }

    private fun summaryRef(kind: String): DocumentReference {
        return db.collection("summaries").document(kind)
    }

    fun getLivestock(uid: String): Task<List<Livestock>?> {
        return livestockRef(uid).get().continueWith {
            if (it.result?.exists() == true) {
                val result = arrayListOf<Livestock>()
                it.result?.data?.map { map ->
                    if (map.value is Long) {
                        result.add(Livestock(map.key, map.value as Long))
                    }
                }
                return@continueWith result
            } else {
                return@continueWith null
            }
        }
    }

    fun getLivestock(callBack: GetDataCallBack<List<Livestock>>) {
        val myUid = AuthUtils().uid

        if (myUid == null) {
            callBack.onCanceled("로그인 정보가 없습니다.")
            return
        }

        livestockRef(myUid).addSnapshotListener { documentSnapshot, error ->
            if (error != null) {
                callBack.onCanceled("cannot get data: " + error.message)
                return@addSnapshotListener
            }

            val livestocks = arrayListOf<Livestock>()

            documentSnapshot?.data?.forEach { data ->
                val kind = data.key
                val count = data.value as Long
                livestocks.add(Livestock(kind, count))
            }
            callBack.onDataReceived(livestocks)
        }
    }

    fun saveLivestock(data: ArrayList<Livestock>, callBack: SaveDataCallBack) {
        val uid = AuthUtils().uid

        if (uid.isNullOrEmpty()) {
            callBack.onCanceled("로그인 정보가 없습니다.")
            return
        }


        val livestocks = hashMapOf<String, Long>()
        data.forEach { livestock ->
            livestocks[livestock.kind] = livestock.count
        }

        livestockRef(uid).set(livestocks).addOnCompleteListener {
            if (!it.isSuccessful) {
                callBack.onCanceled("cannot save livestock data")
                return@addOnCompleteListener
            }
            callBack.onSuccess("Successfully saved livestock data. total:${livestocks.size}")
        }
    }

    fun updateLivestock(livestock: Livestock, callBack: SaveDataCallBack) {
        val uid = AuthUtils().uid

        if (uid.isNullOrEmpty()) {
            callBack.onCanceled("로그인 정보가 없습니다.")
            return
        }

        livestockRef(uid).get().addOnCompleteListener {
            if (!it.isSuccessful) {
                callBack.onCanceled("Cannot get data")
                return@addOnCompleteListener
            }
            val livestocks = arrayListOf<Livestock>()
            it.result?.data?.forEach { map ->
                livestocks.add(Livestock(map.key, map.value as Long))
            }
            livestocks.remove(livestocks.find { current -> current.kind == livestock.kind })
            livestocks.add(livestock)
            livestocks.sortBy { livestock.kind }
            saveLivestock(livestocks, callBack)
        }
    }

    fun getSummary(kind: String, callBack: GetDataCallBack<String>) {
        summaryRef(kind).get().addOnCompleteListener {
            if (!it.isSuccessful) {
                callBack.onCanceled("${kind}에 대한 정보를 불러오는데 실패했습니다.")
                return@addOnCompleteListener
            }

            val data = it.result?.data?.get("content") as String?
            if (!data.isNullOrEmpty()) {
                callBack.onDataReceived(data)
            } else {
                callBack.onCanceled("아직 개요가 작성되지 않았습니다.")
            }
            return@addOnCompleteListener
        }
    }
}