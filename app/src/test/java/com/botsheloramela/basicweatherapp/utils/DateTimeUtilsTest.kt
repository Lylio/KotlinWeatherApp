package com.botsheloramela.basicweatherapp.utils

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateTimeUtilsTest {

    @Test
    fun `getCurrentDate - returns formatted date`() {
        val expectedDate = LocalDate.now()
            .format(
                DateTimeFormatter.ofPattern(
                    "d MMMM, EEEE"
                )
            )

        val result = DateTimeUtils.getCurrentDate()

        assertEquals(expectedDate, result)
    }

    @Test
    fun `parseDtTxtToTimestamp - parses date time to timestamp`() {
        val dtTxt = "2024-09-07 12:00:00"

        val expectedTimestamp = LocalDateTime
            .parse(
                dtTxt,
                DateTimeFormatter.ofPattern(
                    "yyyy-MM-dd HH:mm:ss"
                )
            )
            .atZone(ZoneId.systemDefault())
            .toEpochSecond()

        val result =
            DateTimeUtils.parseDtTxtToTimestamp(dtTxt)

        assertEquals(expectedTimestamp, result)
    }

    @Test
    fun `parseDtTxtToHour - parses date time to hour`() {
        val dtTxt = "2024-09-07 12:00:00"
        val expectedHour = "12 PM"

        val result =
            DateTimeUtils.parseDtTxtToHour(dtTxt)

        assertEquals(expectedHour, result)
    }

    @Test
    fun `parseDtTxtToDayMonth - parses date time to day and month`() {
        val dtTxt = "2024-09-07 12:00:00"
        val expectedDayMonth = "7 September"

        val result =
            DateTimeUtils.parseDtTxtToDayMonth(dtTxt)

        assertEquals(expectedDayMonth, result)
    }
}