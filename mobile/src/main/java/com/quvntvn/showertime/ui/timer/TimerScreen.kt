package com.quvntvn.showertime.ui.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quvntvn.showertime.data.UserPreferencesRepository

@Composable
fun TimerScreen(
    programId: String,
    repository: UserPreferencesRepository,
    onNavigateToSummary: (Int) -> Unit
) {
    val context = LocalContext.current
    val viewModel: TimerViewModel = viewModel(factory = TimerViewModelFactory(programId, repository, context))
    val state by viewModel.timerState.collectAsState()

    if (state.isFinished) {
        LaunchedEffect(state.isFinished) {
            onNavigateToSummary(state.totalProgramDuration)
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Montez le volume !",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Des sons vous guideront à chaque étape."
            )
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                text = state.currentStepName,
                style = MaterialTheme.typography.headlineLarge
            )
            Text(
                text = "${state.stepTimeRemaining}s",
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { if (state.isPaused) viewModel.resume() else viewModel.pause() }) {
                Text(if (state.isPaused) "Reprendre" else "Pause")
            }
        }
    }
}
