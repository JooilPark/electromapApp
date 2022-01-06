package com.sunday.electromapapp

import android.util.Log
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val nowTime = LocalTime.now()
        val weeklyTimeStart: LocalTime = LocalTime.of(5, 0)
        val weeklyTimeEnd: LocalTime = day24change(LocalTime.of(1, 0))
        System.out.println("tiem = $weeklyTimeStart $$ $weeklyTimeEnd")
        System.out.println("tiem = ${weeklyTimeStart.isBefore(nowTime)} $$ ${weeklyTimeEnd.isAfter(nowTime)}")
    }

    private fun day24change(endtime: LocalTime): LocalTime {
        if (endtime.hour != 0 && endtime.hour <= 12) {
            return LocalTime.of(endtime.hour + 12,0)
        }
        return endtime
    }
}