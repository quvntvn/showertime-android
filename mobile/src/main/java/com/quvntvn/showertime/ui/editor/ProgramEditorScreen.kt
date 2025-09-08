package com.quvntvn.showertime.ui.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quvntvn.showertime.common.data.RoutineStep
import com.quvntvn.showertime.data.UserPreferencesRepository
import com.quvntvn.showertime.sync.DataSyncRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramEditorScreen(
    programId: String?,
    repository: UserPreferencesRepository,
    dataSyncRepository: DataSyncRepository,
    onSave: () -> Unit
) {
    val viewModel: ProgramEditorViewModel = viewModel(factory = ProgramEditorViewModelFactory(programId, repository, dataSyncRepository))
    val program by viewModel.programState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (programId == null) "Nouveau Programme" else "Modifier le Programme") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.saveProgram(onSave) }) {
                Text("Sauver")
            }
        }
    ) { padding ->
        program?.let { prog ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    OutlinedTextField(
                        value = prog.name,
                        onValueChange = { viewModel.updateName(it) },
                        label = { Text("Nom du programme") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                itemsIndexed(prog.steps) { index, step ->
                    StepEditor(
                        step = step,
                        onStepChange = { updatedStep -> viewModel.updateStep(index, updatedStep) },
                        onRemove = { viewModel.removeStep(index) }
                    )
                }

                item {
                    Button(onClick = { viewModel.addStep() }) {
                        Text("Ajouter une étape")
                    }
                }
            }
        }
    }
}

@Composable
fun StepEditor(
    step: RoutineStep,
    onStepChange: (RoutineStep) -> Unit,
    onRemove: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = step.name,
                onValueChange = { onStepChange(step.copy(name = it)) },
                label = { Text("Nom de l'étape") }
            )
            OutlinedTextField(
                value = step.durationInSeconds.toString(),
                onValueChange = { onStepChange(step.copy(durationInSeconds = it.toIntOrNull() ?: 0)) },
                label = { Text("Durée (secondes)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            IconButton(onClick = onRemove, modifier = Modifier.align(Alignment.End)) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer l'étape")
            }
        }
    }
}

class ProgramEditorViewModelFactory(
    private val programId: String?,
    private val repository: UserPreferencesRepository,
    private val dataSyncRepository: DataSyncRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProgramEditorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProgramEditorViewModel(programId, repository, dataSyncRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
