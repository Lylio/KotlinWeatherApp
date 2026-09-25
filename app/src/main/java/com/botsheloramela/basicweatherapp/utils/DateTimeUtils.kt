package com.botsheloramela.basicweatherapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateTimeUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    private val dateFormat =
        DateTimeFormatter.ofPattern(
            "d MMMM, EEEE",
            Locale.ENGLISH
        )

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        return currentDate.format(dateFormat)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDtTxtToTimestamp(dtTxt: String): Long {
        val formatter =
            DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss",
                Locale.ENGLISH
            )

        val localDateTime =
            LocalDateTime.parse(dtTxt, formatter)

        return localDateTime
            .atZone(ZoneId.systemDefault())
            .toEpochSecond()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDtTxtToHour(dtTxt: String): String {
        val formatter =
            DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss",
                Locale.ENGLISH
            )

        val localDateTime =
            LocalDateTime.parse(dtTxt, formatter)

        return localDateTime.format(
            DateTimeFormatter.ofPattern(
                "h a",
                Locale.ENGLISH
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseDtTxtToDayMonth(dtTxt: String): String {
        val formatter =
            DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss",
                Locale.ENGLISH
            )

        val localDateTime =
            LocalDateTime.parse(dtTxt, formatter)

        return localDateTime.format(
            DateTimeFormatter.ofPattern(
                "d MMMM",
                Locale.ENGLISH
            )
        )
    }
}