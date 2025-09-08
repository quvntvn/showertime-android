package com.quvntvn.showertime.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.quvntvn.showertime.common.data.AlertPreference
import com.quvntvn.showertime.common.data.HairLength
import com.quvntvn.showertime.common.data.RoutineProgram
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import androidx.datastore.preferences.core.booleanPreferencesKey

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferencesRepository(private val context: Context) {

    private object PreferencesKeys {
        val HAIR_LENGTH = stringPreferencesKey("hair_length")
        val ALERT_PREFERENCE = stringPreferencesKey("alert_preference")
        val IS_CONFIGURED = booleanPreferencesKey("is_configured")
        val CUSTOM_PROGRAMS = stringPreferencesKey("custom_programs")
    }

    val customProgramsFlow: Flow<List<RoutineProgram>> = context.dataStore.data
        .map { preferences ->
            val jsonString = preferences[PreferencesKeys.CUSTOM_PROGRAMS] ?: "[]"
            Json.decodeFromString<List<RoutineProgram>>(jsonString)
        }

    suspend fun saveCustomPrograms(programs: List<RoutineProgram>) {
        val jsonString = Json.encodeToString(programs)
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CUSTOM_PROGRAMS] = jsonString
        }
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .map { preferences ->
            val hairLength = HairLength.valueOf(
                preferences[PreferencesKeys.HAIR_LENGTH] ?: HairLength.MI_LONGS.name
            )
            val alertPreference = AlertPreference.valueOf(
                preferences[PreferencesKeys.ALERT_PREFERENCE] ?: AlertPreference.VIBREUR_ET_SON.name
            )
            val isConfigured = preferences[PreferencesKeys.IS_CONFIGURED] ?: false
            UserPreferences(hairLength, alertPreference, isConfigured)
        }

    private suspend fun markConfigured() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_CONFIGURED] = true
        }
    }

    suspend fun updateHairLength(hairLength: HairLength) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.HAIR_LENGTH] = hairLength.name
        }
        markConfigured()
    }

    suspend fun updateAlertPreference(alertPreference: AlertPreference) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ALERT_PREFERENCE] = alertPreference.name
        }
        markConfigured()
    }
}

data class UserPreferences(
    val hairLength: HairLength,
    val alertPreference: AlertPreference,
    val isConfigured: Boolean
)
