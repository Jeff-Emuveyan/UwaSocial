package com.bellogate_caliphate.uwasocial.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bellogate_caliphate.uwasocial.core.utils.TimeUtils
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimeUtilsMicrobenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Test
    fun benchmarkRelativeTimeFormatting() {
        benchmarkRule.measureRepeated {
            val formatted = TimeUtils.formatRelativeTime(12345L)
            check(formatted.isNotEmpty())
        }
    }
}
