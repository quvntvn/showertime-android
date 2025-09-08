package com.quvntvn.showertime.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.quvntvn.showertime.common.data.AlertPreference
import com.quvntvn.showertime.common.data.HairLength
import com.quvntvn.showertime.data.UserPreferences
import com.quvntvn.showertime.data.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    val uiState: StateFlow<UserPreferences> = userPreferencesRepository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserPreferences(HairLength.MI_LONGS, AlertPreference.VIBREUR_ET_SON, false)
        )

    fun updateHairLength(hairLength: HairLength) {
        viewModelScope.launch {
            userPreferencesRepository.updateHairLength(hairLength)
        }
    }

    fun updateAlertPreference(alertPreference: AlertPreference) {
        viewModelScope.launch {
            userPreferencesRepository.updateAlertPreference(alertPreference)
        }
    }
}

class SettingsViewModelFactory(private val repository: UserPreferencesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
