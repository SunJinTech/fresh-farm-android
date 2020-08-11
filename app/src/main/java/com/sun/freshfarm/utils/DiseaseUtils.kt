package com.sun.freshfaram.utils

import android.util.Log
import com.sun.freshfarm.Constants
import com.sun.freshfarm.database.*
import com.sun.freshfarm.utils.ReportUtils
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class DiseaseUtils {
    private val db = Firebase.firestore

    fun diseasesRef(): CollectionReference {
        return db.collection("diseases")
    }

    fun diseaseRef(diseaseCode: String): DocumentReference {
        return diseasesRef().document(diseaseCode)
    }

//    fun getDisease(code : String){
//
//    }

    fun getDiseases(kind: String?, callBack: GetDataCallBack<List<Disease>>) {
        var query = diseasesRef()
            .orderBy("occureCount", Query.Direction.DESCENDING).get()

        if (!kind.isNullOrEmpty() && Constants.toKorean(kind) != "ERROR") {
            query = diseasesRef().whereArrayContains("targetKinds", Constants.toKorean(kind))
                .orderBy("occureCount", Query.Direction.DESCENDING).get()
        }

        query.addOnCompleteListener {
            if (!it.isSuccessful) {
                callBack.onCanceled("질병 목록을 불러올 수 없습니다.")
                Log.e("get disease error on db", it.exception?.message)
                return@addOnCompleteListener
            }
            val results = arrayListOf<Disease>()
            it.result?.documents?.forEach { snapshot ->
                val current = snapshot.toObject<Disease>()
                if (current != null) {
                    results.add(current)
                }
            }
            if (results.isNotEmpty()) {
                callBack.onDataReceived(results)
            } else {
                callBack.onCanceled("질병 목록이 존재하지 않습니다.")
            }
        }
    }

    fun getDisease(code: String, callBack: GetDataCallBack<Disease>) {
        if (code.isEmpty()) {
            callBack.onCanceled("질병 코드가 존재하지 않습니다.")
            return
        }
        diseaseRef(code).get().addOnCompleteListener {
            if (!it.isSuccessful) {
                callBack.onCanceled("질병 정보를 불러올 수 없습니다.")
                return@addOnCompleteListener
            }
            val result = it.result?.toObject<Disease>()
            if (result != null) {
                callBack.onDataReceived(result)
            } else {
                callBack.onCanceled("질병 정보가 존재하지 않습니다")
            }
        }
    }
}