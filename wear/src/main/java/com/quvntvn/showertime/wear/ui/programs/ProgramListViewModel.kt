package com.quvntvn.showertime.wear.ui.programs

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.quvntvn.showertime.common.data.RoutineProgram
import com.quvntvn.showertime.wear.data.WearProgramRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProgramListViewModel(repository: WearProgramRepository) : ViewModel() {

    val programs: StateFlow<List<RoutineProgram>> = repository.programsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )
}

class ProgramListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProgramListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProgramListViewModel(WearProgramRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
