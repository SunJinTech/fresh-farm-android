package com.sun.freshfarm.database

data class Disease(
    var diseaseCode: String = "",
    var diseaseName: String = "",
    var englishDiseaseName: String? = "",
    var occureCount: Long? = null,
    var recentCount: Long? = null,
    var summary: String? = null,
    var symptoms: List<String>? = null,
    var targetKinds: List<String>? = null
)