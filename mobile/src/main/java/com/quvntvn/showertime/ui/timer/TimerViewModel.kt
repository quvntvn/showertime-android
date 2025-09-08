package com.quvntvn.showertime.ui.timer

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.quvntvn.showertime.data.UserPreferencesRepository
import com.quvntvn.showertime.common.engine.RoutineEngine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TimerViewModel(
    private val programId: String,
    private val repository: UserPreferencesRepository,
    context: Context
) : ViewModel() {

    private val routineEngine = RoutineEngine()
    val timerState = routineEngine.timerState

    init {
        viewModelScope.launch {
            val defaultPrograms = com.quvntvn.showertime.common.data.DefaultPrograms.programs
            val customPrograms = repository.customProgramsFlow.first()
            val allPrograms = defaultPrograms + customPrograms
            val program = allPrograms.find { it.id == programId }
            program?.let {
                routineEngine.start(it)
            }
        }

        // Play sounds on step change
        timerState.onEach { state ->
            if (state.isTransitioning) {
                playSound("transition_start")
            } else if (state.currentStepIndex != -1) {
                playSound("step_start")
            }
            if (state.stepTimeRemaining == 0 && state.currentStepIndex != -1) {
                playSound("step_end")
            }
        }.distinctUntilChanged { old, new ->
            old.currentStepName == new.currentStepName && old.isTransitioning == new.isTransitioning
        }.launchIn(viewModelScope)
    }

    private fun playSound(soundType: String) {
        // In a real app, this would use MediaPlayer or SoundPool to play sounds from res/raw
        Log.d("TimerViewModel", "Playing sound: $soundType")
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
    private val repository: UserPreferencesRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TimerViewModel(programId, repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
