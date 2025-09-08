package com.quvntvn.showertime.wear.ui.timer

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.quvntvn.showertime.wear.data.WearProgramRepository
import com.quvntvn.showertime.common.engine.RoutineEngine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TimerViewModel(
    programId: String,
    repository: WearProgramRepository,
    context: Context
) : ViewModel() {

    private val routineEngine = RoutineEngine()
    val timerState = routineEngine.timerState
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    init {
        viewModelScope.launch {
            val program = repository.programsFlow.first().find { it.id == programId }
            program?.let {
                routineEngine.start(it)
            }
        }

        // Vibrate on step change
        timerState.onEach { state ->
            if (state.currentStepIndex != -1) {
                vibrate()
            }
        }.distinctUntilChanged { old, new -> old.currentStepName == new.currentStepName }
         .launchIn(viewModelScope)
    }

    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 200, 100, 200), -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(longArrayOf(0, 200, 100, 200), -1)
        }
    }

    fun pause() = routineEngine.pause()
    fun resume() = routineEngine.resume()

    override fun onCleared() {
        super.onCleared()
        routineEngine.stop()
    }
}

class TimerViewModelFactory(
    private val programId: String,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimerViewModel(programId, WearProgramRepository(context), context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
