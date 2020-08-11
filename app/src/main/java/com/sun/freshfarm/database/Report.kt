package com.sun.freshfarm.database

import kotlin.String


data class Report(
    var id: String? = null,
    var reporterUid: String = "",
    var reportTime: Long = -1,
    var reportedUid: String = "",
    var reportFarmName : String? = null,
    var reportFarmLocation : String? = null,
    var diseaseName : String? = null,
    var diseaseCode: String = "",
    var occurrenceCount: Long = -1,
    var livestockKinds: List<String> = listOf(),
    var symptoms: List<String> = listOf()
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "reporterUid" to reporterUid,
            "reportTime" to reportTime,
            "diseaseCode" to diseaseCode,
            "occurrenceCount" to occurrenceCount,
            "livestockKinds" to livestockKinds,
            "symptoms" to symptoms
        )
    }
}