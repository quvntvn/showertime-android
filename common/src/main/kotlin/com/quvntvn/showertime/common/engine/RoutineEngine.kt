package com.quvntvn.showertime.common.engine

import com.quvntvn.showertime.common.data.RoutineProgram
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class TimerState(
    val currentStepIndex: Int = -1,
    val currentStepName: String = "Prêt ?",
    val stepDuration: Int = 0,
    val stepTimeRemaining: Int = 0,
    val totalTimeRemaining: Int = 0,
    val progress: Float = 0f,
    val isPaused: Boolean = false,
    val isFinished: Boolean = false,
    val isTransitioning: Boolean = false,
    val totalProgramDuration: Int = 0
)

class RoutineEngine {

    private val _timerState = MutableStateFlow(TimerState())
    val timerState = _timerState.asStateFlow()

    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun start(program: RoutineProgram) {
        stop() // Stop any existing routine
        val totalProgramDuration = program.steps.sumOf { it.durationInSeconds } + (program.steps.size - 1) * program.pausesInSeconds

        timerJob = scope.launch {
            _timerState.value = TimerState(totalTimeRemaining = totalProgramDuration, totalProgramDuration = totalProgramDuration)

            for ((index, step) in program.steps.withIndex()) {
                runStep(index, step, isTransition = false)

                if (index < program.steps.size - 1) {
                    val pauseStep = com.quvntvn.showertime.common.data.RoutineStep("Pause", program.pausesInSeconds)
                    runStep(index, pauseStep, isTransition = true)
                }
            }

            // Finish
            _timerState.value = _timerState.value.copy(
                currentStepName = "Terminé !",
                isFinished = true,
                progress = 1f,
                stepTimeRemaining = 0
            )
        }
    }

    private suspend fun runStep(index: Int, step: com.quvntvn.showertime.common.data.RoutineStep, isTransition: Boolean) {
        _timerState.value = _timerState.value.copy(
            currentStepIndex = index,
            currentStepName = step.name,
            stepDuration = step.durationInSeconds,
            stepTimeRemaining = step.durationInSeconds,
            isTransitioning = isTransition
        )
        countdown(step.durationInSeconds)
    }

    private suspend fun countdown(seconds: Int) {
        for (i in seconds downTo 1) {
            while (_timerState.value.isPaused) {
                delay(100) // Check for resume every 100ms
            }
            delay(1000)
            val currentState = _timerState.value
            val newStepTime = currentState.stepTimeRemaining - 1
            val newTotalTime = currentState.totalTimeRemaining - 1
            _timerState.value = currentState.copy(
                stepTimeRemaining = newStepTime,
                totalTimeRemaining = newTotalTime,
                progress = 1f - (newTotalTime.toFloat() / totalProgramDuration.toFloat().takeIf { it > 0 } ?: 1f)
            )
        }
    }

    fun pause() {
        _timerState.value = _timerState.value.copy(isPaused = true)
    }

    fun resume() {
        _timerState.value = _timerState.value.copy(isPaused = false)
    }

    fun stop() {
        timerJob?.cancel()
        timerJob = null
        _timerState.value = TimerState()
    }
}
