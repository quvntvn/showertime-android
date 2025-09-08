package com.quvntvn.showertime.ui.summary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.math.roundToInt

class SummaryViewModel(totalTimeInSeconds: Int) : ViewModel() {

    val totalTime = formatTime(totalTimeInSeconds)
    val waterUsed = calculateWaterUsed(totalTimeInSeconds)

    private fun formatTime(seconds: Int): String {
        val mins = seconds / 60
        val secs = seconds % 60
        return String.format("%d min %02d s", mins, secs)
    }

    private fun calculateWaterUsed(seconds: Int): String {
        // Average shower flow rate: 9 liters/minute
        val flowRate = 9.0
        val waterUsedLiters = (seconds / 60.0) * flowRate
        return "${waterUsedLiters.roundToInt()} litres utilisés (estimation)"
    }
}

class SummaryViewModelFactory(private val totalTimeInSeconds: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SummaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SummaryViewModel(totalTimeInSeconds) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
