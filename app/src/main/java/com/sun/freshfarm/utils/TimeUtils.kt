package com.sun.freshfarm.utils

import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TimeUtils {
    class DateTime(
        var year: Int,
        var month: Int,
        var dayOfMonth: Int,
        var hourOfDay: Int,
        var minute: Int
    )

    fun createCurrentDateTime(): DateTime {
        val current = LocalDateTime.now()

        return DateTime(
            current.year,
            current.month.ordinal,
            current.dayOfMonth,
            current.hour,
            current.minute
        )
    }

    fun dateToString(year: Int, month: Int, dayOfMonth: Int): String {
        return "${year}/${month + 1}/${dayOfMonth}"
    }

    fun getTimestamp(dateTime: DateTime): Long {
        return Timestamp(
            dateTime.year - 1900,
            dateTime.month,
            dateTime.dayOfMonth,
            dateTime.hourOfDay,
            dateTime.minute,
            0,
            0
        ).time
    }

    fun getTimestamp(): Long {
        val dateTime = createCurrentDateTime()
        return Timestamp(
            dateTime.year - 1900,
            dateTime.month,
            dateTime.dayOfMonth,
            dateTime.hourOfDay,
            dateTime.minute,
            0,
            0
        ).time
    }

    fun timeToString(hourOfDay: Int, minute: Int): String {
        val hour: String = if (hourOfDay > 10) {
            "$hourOfDay"
        } else {
            "0$hourOfDay"
        }

        val min: String = if (minute > 10) {
            "$minute"
        } else {
            "0$minute"
        }

        return "$hour:$min"
    }

    fun getDateTime(timestamp: Long): DateTime {
        val timeObject =
            Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime()
        return DateTime(
            timeObject.year,
            timeObject.month.ordinal,
            timeObject.dayOfMonth,
            timeObject.hour,
            timeObject.minute
        )
    }

    fun toDateString(timestamp: Long): String {
        val dateTime = getDateTime(timestamp)
        return dateToString(dateTime.year, dateTime.month, dateTime.dayOfMonth)
    }
}