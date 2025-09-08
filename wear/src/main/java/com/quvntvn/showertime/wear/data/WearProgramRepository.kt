package com.quvntvn.showertime.wear.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.quvntvn.showertime.common.data.DefaultPrograms
import com.quvntvn.showertime.common.data.RoutineProgram
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "programs")

class WearProgramRepository(private val context: Context) {

    private object PreferencesKeys {
        val PROGRAMS = stringPreferencesKey("programs")
    }

    val programsFlow: Flow<List<RoutineProgram>> = context.dataStore.data
        .map { preferences ->
            val jsonString = preferences[PreferencesKeys.PROGRAMS]
            if (jsonString != null) {
                Json.decodeFromString<List<RoutineProgram>>(jsonString)
            } else {
                // If no programs are saved, return the defaults
                DefaultPrograms.programs
            }
        }

    suspend fun savePrograms(programs: List<RoutineProgram>) {
        val jsonString = Json.encodeToString(programs)
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.PROGRAMS] = jsonString
        }
    }
}
