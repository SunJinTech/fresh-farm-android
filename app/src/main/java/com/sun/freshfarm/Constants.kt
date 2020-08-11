package com.sun.freshfarm

import com.sun.freshfarm.database.Livestock
import com.sun.freshfarm.database.Profile
import com.sun.freshfarm.livestock.select.LivestockSelectActivity

class Constants {

    enum class KINDS_STRING(val value: String) {
        COW("COW"),
        DOG("DOG"),
        PIG("PIG"),
        HEN("HEN"),
        SHEEP("SHEEP"),
        BEE("BEE")
    }

    companion object {
        val DOG = "DOG"
        val COW = "COW"

        var isAdmin = false

        var myProfile: Profile? = null

        var livestocks = listOf<String>()

        val livestocks_kor = listOf<String>(
            "돼지", "소", "닭", "벌", "양"
        )

        fun toKorean(kind: String): String {
            return when (kind) {
                "COW" -> "소"
                "DOG" -> "개"
                "PIG" -> "돼지"
                "HEN" -> "닭"
                "SHEEP" -> "양"
                "BEE" -> "벌"
                else -> "ERROR"
            }
        }

        fun toEnglish(kind: String): String {
            return when (kind) {
                "소" -> "COW"
                "돼지" -> "PIG"
                "닭" -> "HEN"
                "양" -> "SHEEP"
                "벌" -> "BEE"
                else -> "ERROR"
            }
        }

        fun convertId(kind: String): Int? {
            return KIND_TO_ID[kind]
        }


        val KIND_TO_ID = hashMapOf<String, Int>(
            "COW" to R.drawable.cow_white,
            "DOG" to R.drawable.dog_white,
            "PIG" to R.drawable.pig_white,
            "HEN" to R.drawable.hen_white,
            "SHEEP" to R.drawable.sheep_white,
            "BEE" to R.drawable.bee_white
        )
    }
}