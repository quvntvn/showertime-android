package com.quvntvn.showertime.common.data

import kotlinx.serialization.Serializable

@Serializable
data class RoutineStep(
    val name: String,
    val durationInSeconds: Int
)

@Serializable
data class RoutineProgram(
    val id: String, // Unique ID for the program
    val name: String,
    val steps: List<RoutineStep>,
    val pausesInSeconds: Int = 10
)

data class UserProfile(
    val hairLength: HairLength,
    val alertPreference: AlertPreference
)

enum class HairLength(val displayName: String) {
    TRES_COURTS("Très courts"),
    COURTS("Courts"),
    MI_LONGS("Mi-longs"),
    LONGS("Longs")
}

enum class AlertPreference(val displayName: String) {
    VIBREUR_ET_SON("Vibreur + Son"),
    VIBREUR_SEUL("Vibreur seul")
}
