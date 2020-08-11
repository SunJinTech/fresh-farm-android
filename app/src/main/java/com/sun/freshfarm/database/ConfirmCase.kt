package com.sun.freshfarm.database

data class ConfirmCase(
    var id: String? = null,
    val diseaseCode: String = "",
    val diseaseName: String = "",
    val farmName: String = "",
    val farmLocation: String = "",
    val farmLat: Double = 0.0,
    val farmLng: Double = 0.0,
    val livestockKind: String = "",
    val occureCount: Long? = null,
    val occureDate: Long? = null
)