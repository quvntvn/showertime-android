package com.quvntvn.showertime.ui.programs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quvntvn.showertime.common.data.DefaultPrograms
import com.quvntvn.showertime.common.data.RoutineProgram
import com.quvntvn.showertime.data.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn

class ProgramListViewModel(repository: UserPreferencesRepository) : ViewModel() {

    private val _programs = MutableStateFlow<List<RoutineProgram>>(emptyList())
    val programs: StateFlow<List<RoutineProgram>> = _programs

    val canAddProgram: Boolean
        // TODO: PREMIUM FEATURE - In a real app, this would check for premium status.
        // For now, we allow only one custom program.
        get() = _programs.value.count { it !in DefaultPrograms.programs } < 1

    init {
        repository.customProgramsFlow.combine(MutableStateFlow(DefaultPrograms.programs)) { custom, default ->
            default + custom
        }.onEach { combinedList ->
            _programs.value = combinedList
        }.launchIn(viewModelScope)
    }
}
