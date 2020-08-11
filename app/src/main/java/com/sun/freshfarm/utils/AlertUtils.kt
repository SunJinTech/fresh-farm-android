package com.sun.freshfarm.utils

import com.sun.freshfarm.database.Alert
import com.sun.freshfarm.database.GetDataCallBack
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class AlertUtils {
    private val db = Firebase.firestore

    fun alertsRef(): CollectionReference {
        return db.collection("alerts")
    }

    fun getAlerts(callBack: GetDataCallBack<List<Alert>>) {
        alertsRef().addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                callBack.onCanceled("cannot get alert list: ${error.message}")
                return@addSnapshotListener
            }

            val alerts = arrayListOf<Alert>()
            querySnapshot?.documents?.forEach { snapshot ->
                snapshot.toObject<Alert>()?.let { alert ->
                    alerts.add(alert)
                }
            }
            callBack.onDataReceived(alerts)
        }
    }


    fun getAlerts(kind: String, callBack: GetDataCallBack<List<Alert>>) {
        alertsRef().addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                callBack.onCanceled("cannot get alert list: ${error.message}")
                return@addSnapshotListener
            }
            val alerts = arrayListOf<Alert>()
            querySnapshot?.documents?.forEach { snapshot ->
                snapshot.toObject<Alert>()?.let { alert ->
                    if (alert.kind == kind) {
                        alerts.add(alert)
                    }
                }
            }
            callBack.onDataReceived(alerts)
        }
    }
}