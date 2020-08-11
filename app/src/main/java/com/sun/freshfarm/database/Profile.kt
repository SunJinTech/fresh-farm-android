package com.sun.freshfarm.database

import kotlin.String


data class Profile(
//    about user
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    var phoneNumber: String = "",
//    about farm
    var farmName: String = "",
    var farmLocation: String = "",
//    y
    var farmLat: Double? = null,
//    x
    var farmLng: Double? = null,
//    optional
    var introduce: String? = ""
)