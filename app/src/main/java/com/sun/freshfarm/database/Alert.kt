package com.sun.freshfarm.database

import kotlin.String

data class Alert(
    var message: String = "",
//    kind : for what's the kind of livestock
    var kind: String = "",
//    type : Level of Alert(ex WARNING, caution, normal)
    var level: String = ""
)