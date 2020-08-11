package com.sun.freshfarm.utils


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.sun.freshfaram.utils.DiseaseUtils
import com.sun.freshfarm.Constants
import com.sun.freshfarm.database.ConfirmCase
import com.sun.freshfarm.database.GetDataCallBack
import com.sun.freshfarm.database.Report

class CaseUtils {
    private val db = Firebase.firestore

    fun casesRef(): CollectionReference {
        return db.collection("confirmCases")
    }

    fun caseRef(id: String): DocumentReference {
        return casesRef().document(id)
    }

    fun getCases(size: Long): Task<List<ConfirmCase>> {
        return casesRef().orderBy("farmLat").limit(size).get().continueWith {
            if (it.isSuccessful) {
                val result = arrayListOf<ConfirmCase>()
                val cases = it.result?.toObjects<ConfirmCase>()
                cases?.map { it1 ->
                    result.add(it1)
                }
                return@continueWith result
            } else {
                return@continueWith null
            }
        }
    }

    fun getCases(
        size: Long,
        kind: String?,
        diseaseCode: String?,
        callBack: GetDataCallBack<List<ConfirmCase>>
    ) {
        var query = casesRef().limit(size)
            .orderBy("occureDate", Query.Direction.DESCENDING)

        if (kind != null) {
            query = casesRef().whereEqualTo("livestockKind", kind)
                .orderBy("occureDate", Query.Direction.DESCENDING)
        }
        if (diseaseCode != null) {
            query = casesRef().whereEqualTo("diseaseCode", diseaseCode)
                .orderBy("occureDate", Query.Direction.DESCENDING)
        }

        if (kind == null && diseaseCode == null) {
            val kinds = arrayListOf<String>()
            Constants.livestocks.map {
                kinds.add(Constants.toKorean(it))

            }
            query = query.whereIn("livestockKind", kinds)
        }


        query.limit(size).get()
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    callBack.onCanceled("확진 정보를 읽어오는데 실패했습니다.")
                    return@addOnCompleteListener
                }

                val results = arrayListOf<ConfirmCase>()

                it.result?.documents?.map { document ->
                    var case = document.toObject<ConfirmCase>()
                    if (case != null) {
                        case.id = document.id
                        results.add(case)
                    }
                }

                if (results.size > 0) {
                    callBack.onDataReceived(results)
                } else {
                    callBack.onCanceled("확진 정보가 없습니다.")
                }
            }
    }

    fun getCase(id: String, callBack: GetDataCallBack<ConfirmCase>) {
        caseRef(id).get().addOnCompleteListener {
            if (!it.isSuccessful) {
                callBack.onCanceled("질병 발생 정보를 받아올 수 없습니다.")
                return@addOnCompleteListener
            }

            val result = it.result?.toObject<ConfirmCase>()

            if (result != null) {
                callBack.onDataReceived(result)
            } else {
                callBack.onCanceled("질병 발생 정보가 없습니다.")
            }
        }
    }

    fun searchCases(
        diseaseName: String?,
        livestockKind: String?,
        callBack: GetDataCallBack<List<ConfirmCase>>
    ) {
        var query = casesRef().orderBy("occureDate", Query.Direction.DESCENDING)
        if (diseaseName != null) {
            query = query.whereEqualTo("diseaseName", diseaseName)
        }
        if (livestockKind != null) {
            query = query.whereEqualTo("livestockKind", livestockKind)
        }
        query.limit(20).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = arrayListOf<ConfirmCase>()

                    it.result?.documents?.map { documentSnapshot ->
                        val case = documentSnapshot.toObject<ConfirmCase>()
                        if (case != null) {
                            result.add(case)
                        }
                    }
                    if (result.size > 0) {
                        callBack.onDataReceived(result)

                    } else {
                        callBack.onCanceled("데이터가 없습니다.")
                    }
                } else {
                    callBack.onCanceled("정보못찾음")
                }
            }
    }

    fun getSearchConfirmCase(
        selector: String,
        diseaseName: String,
        callBack: GetDataCallBack<List<ConfirmCase>>
    ) {
        val query = casesRef().orderBy("occureDate", Query.Direction.DESCENDING)
            .whereEqualTo(selector, diseaseName)
        query.limit(20).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = arrayListOf<ConfirmCase>()

                    it.result?.documents?.map { documentSnapshot ->
                        val case = documentSnapshot.toObject<ConfirmCase>()
                        if (case != null) {
                            result.add(case)
                        }
                    }
                    if (result.size > 0) {
                        callBack.onDataReceived(result)

                    } else {
                        callBack.onCanceled("데이터가 없습니다.")
                    }
                } else {
                    callBack.onCanceled("정보못찾음")
                }
            }
    }

    fun isDiseaseName(value: String): Task<Boolean> {
        val query = DiseaseUtils().diseasesRef().whereEqualTo("diseaseName", value)
        return query.limit(1).get().continueWith {
            if (it.isSuccessful) {
                if (it.result?.isEmpty != true) {
                    return@continueWith true
                }
            }
            return@continueWith false
        }
    }

    fun checkSearchName(selector: String, name: String, callBack: GetDataCallBack<String>) {
        val query = casesRef().orderBy("occureDate", Query.Direction.DESCENDING)
            .whereEqualTo(selector, name)
        query.limit(20).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val size = it.result?.documents?.size
                    if (size != null && size > 0) {
                        callBack.onDataReceived(selector)
                    }
                } else {
                    callBack.onCanceled("정보못찾음")
                }
            }
    }

    fun getSearchReport(diseaseName: String, callBack: GetDataCallBack<List<Report>>) {
        val query = casesRef().orderBy("occureDate", Query.Direction.DESCENDING)
            .whereEqualTo("diseaseName", diseaseName)
        query.limit(20).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val result = arrayListOf<Report>()

                    it.result?.documents?.map { documentSnapshot ->
                        val case = documentSnapshot.toObject<Report>()
                        if (case != null) {
                            result.add(case)
                        }
                    }
                    if (result.size > 0) {
                        callBack.onDataReceived(result)

                    } else {
                        callBack.onCanceled("데이터가 없습니다.")
                    }
                } else {
                    callBack.onCanceled("정보못찾음")
                }
            }
    }
}