package com.quvntvn.showertime.ui.programs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quvntvn.showertime.common.data.DefaultPrograms
import com.quvntvn.showertime.common.data.RoutineProgram
import com.quvntvn.showertime.data.UserPreferencesRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramListScreen(
    repository: UserPreferencesRepository,
    onProgramClick: (String) -> Unit,
    onAddProgram: () -> Unit,
    onEditProgram: (String) -> Unit
) {
    val viewModel: ProgramListViewModel = viewModel(factory = ProgramListViewModelFactory(repository))
    val programs by viewModel.programs.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Choisissez un programme") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (viewModel.canAddProgram) {
                        onAddProgram()
                    }
                },
                containerColor = if (viewModel.canAddProgram) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter un programme")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(programs) { program ->
                val isCustom = program !in DefaultPrograms.programs
                ProgramCard(
                    program = program,
                    isCustom = isCustom,
                    onClick = {
                        if (isCustom) {
                            onEditProgram(program.id)
                        } else {
                            onProgramClick(program.id)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProgramCard(program: RoutineProgram, isCustom: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = program.name, modifier = Modifier.weight(1f))
            if (isCustom) {
                Icon(Icons.Default.Edit, contentDescription = "Modifier le programme")
            }
        }
    }
}

class ProgramListViewModelFactory(private val repository: UserPreferencesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProgramListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProgramListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
