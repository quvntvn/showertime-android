package com.quvntvn.showertime.ui.summary

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SummaryScreen(
    totalTimeInSeconds: Int,
    onFinish: () -> Unit
) {
    val viewModel: SummaryViewModel = viewModel(factory = SummaryViewModelFactory(totalTimeInSeconds))

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Bravo !",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Temps total : ${viewModel.totalTime}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = viewModel.waterUsed,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(64.dp))
            Button(onClick = onFinish) {
                Text("Terminer")
            }
        }
    }
}
