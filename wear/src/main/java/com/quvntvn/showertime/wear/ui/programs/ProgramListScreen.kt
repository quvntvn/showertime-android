package com.quvntvn.showertime.wear.ui.programs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import com.quvntvn.showertime.common.data.RoutineProgram

@Composable
fun ProgramListScreen(
    onProgramClick: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: ProgramListViewModel = viewModel(factory = ProgramListViewModelFactory(context))
    val programs by viewModel.programs.collectAsState()

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 32.dp, start = 8.dp, end = 8.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(text = "Programmes")
        }
        items(programs) { program ->
            ProgramChip(program = program, onClick = { onProgramClick(program.id) })
        }
    }
}

@Composable
fun ProgramChip(program: RoutineProgram, onClick: () -> Unit) {
    Chip(
        onClick = onClick,
        label = { Text(program.name) }
    )
}
