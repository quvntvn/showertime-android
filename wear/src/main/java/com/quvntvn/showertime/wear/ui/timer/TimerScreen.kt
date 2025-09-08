package com.quvntvn.showertime.wear.ui.timer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.SwipeToDismissBox
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.rememberSwipeToDismissBoxState

@Composable
fun TimerScreen(
    programId: String,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: TimerViewModel = viewModel(factory = TimerViewModelFactory(programId, context))
    val state by viewModel.timerState.collectAsState()

    val swipeToDismissBoxState = rememberSwipeToDismissBoxState()

    Scaffold {
        SwipeToDismissBox(
            state = swipeToDismissBoxState,
            onDismissed = { onFinish() }
        ) { isBackground ->
            if (!isBackground) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = state.progress,
                        modifier = Modifier.size(150.dp),
                        strokeWidth = 8.dp
                    )
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.currentStepName,
                            style = MaterialTheme.typography.title1
                        )
                        Text(
                            text = "${state.stepTimeRemaining}s",
                            style = MaterialTheme.typography.display1
                        )
                    }
                }
            }
        }
    }
}
