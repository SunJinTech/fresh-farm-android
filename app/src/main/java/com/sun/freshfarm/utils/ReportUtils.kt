package com.sun.freshfarm.utils

import com.sun.freshfaram.utils.AuthUtils
import com.sun.freshfarm.database.*
import com.sun.freshfarmadmin.utils.UserUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ReportUtils {
    private val auth = Firebase.auth
    private val db = Firebase.firestore

    fun reportsRef(): CollectionReference {
        return db.collection("reports")
    }

    fun reportRef(id: String): DocumentReference {
        return reportsRef().document(id)
    }

    fun getReport(id: String, callBack: GetDataCallBack<Report>) {
        val query = reportRef(id)
        query.get().addOnCompleteListener {
            if (!it.isSuccessful) {
                callBack.onCanceled("신고 정보를 읽어오는데 실패했습니다.")
                return@addOnCompleteListener
            }

            val result = it.result?.toObject<Report>()
            if (result != null) {
                result.id = it.result?.id
//                result.reporterUid = it.result?.id!!
                callBack.onDataReceived(result)
            } else {
                callBack.onCanceled("신고 정보가 존재하지 않습니다.")
            }
        }
    }

    fun getReports(size: Long, callBack: GetDataCallBack<List<Report>>) {
        reportsRef().orderBy("reportTime", Query.Direction.DESCENDING).limit(size)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    callBack.onCanceled("can not update reports data: ${error.message}")
                    return@addSnapshotListener
                }
                val reports = ArrayList<Report>()
                snapshot?.forEach { reportSnapshot ->
                    val report = reportSnapshot.toObject<Report>()
                    report.id = reportSnapshot?.id.toString()
                    reports.add(report)
                }
                callBack.onDataReceived(reports)
            }
    }


    fun submitReport(
        disease: Disease,
        livestock: Livestock,
        symptoms: List<String>,
        callBack: SaveDataCallBack
    ) {

        val report = Report()

//        about Disease
        report.diseaseCode = disease.diseaseCode
        report.diseaseName = disease.diseaseName
//        report.diseaseName = disease.diseaseName

        report.symptoms = symptoms

//        about livestock
        report.livestockKinds = listOf(livestock.kind)

//        about reportTime
        report.reportTime = TimeUtils().getTimestamp()

        if (!AuthUtils().isLoggedIn) {
            callBack.onCanceled("로그인을 먼저 진행해주세요.")
            return
        }

//        about farm profile
        UserUtils().getProfile(AuthUtils().uid!!, object : GetDataCallBack<Profile> {
            override fun onDataReceived(data: Profile) {
                report.reporterUid = AuthUtils().uid!!
                report.reportFarmName = data.farmName
                report.reportFarmLocation = data.farmLocation

                reportsRef().document().set(report).addOnCompleteListener {
                    if (!it.isSuccessful) {
                        callBack.onCanceled("데이터를 서버에 저장할 수 없습니다.")
                        return@addOnCompleteListener
                    }
                    callBack.onSuccess("성공적으로 신고가 접수되었습니다.")
                }
            }

            override fun onCanceled(message: String) {
                callBack.onCanceled("프로필 정보를 받아올 수 없습니다.")
            }

        })
    }

    fun confirmReport(reportId: String, callBack: SaveDataCallBack) {
        getReport(reportId, object : GetDataCallBack<Report> {
            override fun onDataReceived(data: Report) {
                val confirmCase = ConfirmCase(
                    diseaseCode = data.diseaseCode,
//                    diseaseName = data.diseaseName,
//                    farmName = data.farmName,
//                    farmLocation = data.farmLocation,
//                    livestockKind = data.livestockKind,
//                    occureCount = data.livestockCount,
                    occureDate = data.reportTime
                )
                CaseUtils().casesRef().document().set(confirmCase).addOnCompleteListener {
                    if (it.isSuccessful) {
                        deleteReport(reportId)
                        callBack.onSuccess("정상적으로 확진 처리되었습니다.")
                    } else {
                        callBack.onCanceled("확진 처리에 실패했습니다.")
                    }
                }
            }

            override fun onCanceled(message: String) {
                callBack.onCanceled("신고 정보를 불러올 수 없습니다.")
            }

        })
    }

    fun deleteReport(reportId: String) {
        reportRef(reportId).delete()
    }
}