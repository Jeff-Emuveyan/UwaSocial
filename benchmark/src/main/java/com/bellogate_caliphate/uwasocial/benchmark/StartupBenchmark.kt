package com.bellogate_caliphate.uwasocial.benchmark

import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupBenchmark {

    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun coldStartup() = measureStartup(StartupMode.COLD)

    @Test
    fun warmStartup() = measureStartup(StartupMode.WARM)

    @Test
    fun hotStartup() = measureStartup(StartupMode.HOT)

    private fun measureStartup(mode: StartupMode) {
        benchmarkRule.measureRepeated(
            packageName = "com.bellogate_caliphate.uwasocial",
            metrics = listOf(StartupTimingMetric()),
            iterations = 5,
            startupMode = mode
        ) {
            pressHome()
            startActivityAndWait()
        }
    }
}
