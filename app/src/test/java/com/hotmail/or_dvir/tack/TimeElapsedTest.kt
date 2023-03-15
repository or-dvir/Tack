package com.hotmail.or_dvir.tack

import java.util.Calendar
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TimeElapsedTest {
    private val startCal = Calendar.getInstance()
    @Before
    private fun before() {
        startCal.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
    }

    @Test
    fun elapsedSeconds() {
        val secondsAdded = 25
        val endCal = Calendar.getInstance().apply {
            timeInMillis = startCal.timeInMillis
        }

        endCal.add(Calendar.SECOND, secondsAdded)
        val elapsed = elapsedTime(startCal.timeInMillis, endCal.timeInMillis)

        assertEquals(0, elapsed.first)
        assertEquals(0, elapsed.second)
        assertEquals(secondsAdded, elapsed.third)
    }

    @Test
    fun elapsedMinutes() {
        val minutesAdded = 25
        val endCal = Calendar.getInstance().apply {
            timeInMillis = startCal.timeInMillis
        }

        endCal.add(Calendar.MINUTE, minutesAdded)
        val elapsed = elapsedTime(startCal.timeInMillis, endCal.timeInMillis)

        assertEquals(0, elapsed.first)
        assertEquals(minutesAdded, elapsed.second)
        assertEquals(0, elapsed.third)
    }

    @Test
    fun elapsedHours() {
        val hoursAdded = 25
        val endCal = Calendar.getInstance().apply {
            timeInMillis = startCal.timeInMillis
        }

        endCal.add(Calendar.HOUR_OF_DAY, hoursAdded)
        val elapsed = elapsedTime(startCal.timeInMillis, endCal.timeInMillis)

        assertEquals(hoursAdded, elapsed.first)
        assertEquals(0, elapsed.second)
        assertEquals(0, elapsed.third)
    }
}
