package com.quvntvn.showertime.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quvntvn.showertime.common.data.RoutineProgram
import com.quvntvn.showertime.common.data.RoutineStep
import com.quvntvn.showertime.data.UserPreferencesRepository
import com.quvntvn.showertime.sync.DataSyncRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

class ProgramEditorViewModel(
    private val programId: String?,
    private val repository: UserPreferencesRepository,
    private val dataSyncRepository: DataSyncRepository
) : ViewModel() {

    private val _programState = MutableStateFlow<RoutineProgram?>(null)
    val programState = _programState.asStateFlow()

    init {
        viewModelScope.launch {
            if (programId != null) {
                _programState.value = repository.customProgramsFlow.first().find { it.id == programId }
            } else {
                _programState.value = RoutineProgram(id = UUID.randomUUID().toString(), name = "Nouveau Programme", steps = listOf())
            }
        }
    }

    fun updateName(name: String) {
        _programState.value = _programState.value?.copy(name = name)
    }

    fun addStep() {
        val newStep = RoutineStep(name = "Nouvelle étape", durationInSeconds = 60)
        _programState.value = _programState.value?.copy(steps = _programState.value!!.steps + newStep)
    }

    fun removeStep(index: Int) {
        _programState.value = _programState.value?.copy(
            steps = _programState.value!!.steps.filterIndexed { i, _ -> i != index }
        )
    }

    fun updateStep(index: Int, step: RoutineStep) {
        val newSteps = _programState.value?.steps?.toMutableList()
        if (newSteps != null) {
            newSteps[index] = step
            _programState.value = _programState.value?.copy(steps = newSteps)
        }
    }

    fun saveProgram(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val currentProgram = _programState.value ?: return@launch
            val allPrograms = repository.customProgramsFlow.first().toMutableList()
            val existingIndex = allPrograms.indexOfFirst { it.id == currentProgram.id }
            if (existingIndex != -1) {
                allPrograms[existingIndex] = currentProgram
            } else {
                allPrograms.add(currentProgram)
            }
            repository.saveCustomPrograms(allPrograms)
            dataSyncRepository.syncPrograms(allPrograms)
            onSuccess()
        }
    }
}
