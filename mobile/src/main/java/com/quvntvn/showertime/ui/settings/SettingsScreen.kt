package com.quvntvn.showertime.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quvntvn.showertime.common.data.AlertPreference
import com.quvntvn.showertime.common.data.HairLength
import com.quvntvn.showertime.data.UserPreferencesRepository

@Composable
fun SettingsScreen(
    userPreferencesRepository: UserPreferencesRepository
) {
    val viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(userPreferencesRepository)
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Configuration") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Longueur des cheveux", style = MaterialTheme.typography.titleLarge)
            HairLength.entries.forEach { hairLength ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(hairLength.displayName)
                    RadioButton(
                        selected = uiState.hairLength == hairLength,
                        onClick = { viewModel.updateHairLength(hairLength) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Préférences d'alerte", style = MaterialTheme.typography.titleLarge)
            AlertPreference.entries.forEach { alertPreference ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(alertPreference.displayName)
                    RadioButton(
                        selected = uiState.alertPreference == alertPreference,
                        onClick = { viewModel.updateAlertPreference(alertPreference) }
                    )
                }
            }
        }
    }
}
