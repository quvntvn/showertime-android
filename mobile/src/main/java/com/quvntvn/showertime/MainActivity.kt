package com.quvntvn.showertime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.quvntvn.showertime.common.data.AlertPreference
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.quvntvn.showertime.common.data.AlertPreference
import com.quvntvn.showertime.common.data.HairLength
import com.quvntvn.showertime.data.UserPreferences
import com.quvntvn.showertime.data.UserPreferencesRepository
import com.quvntvn.showertime.ui.editor.ProgramEditorScreen
import com.quvntvn.showertime.ui.programs.ProgramListScreen
import com.quvntvn.showertime.ui.settings.SettingsScreen
import com.quvntvn.showertime.sync.DataSyncRepository
import com.quvntvn.showertime.ui.summary.SummaryScreen
import com.quvntvn.showertime.ui.theme.ShowerTimeTheme
import com.quvntvn.showertime.ui.timer.TimerScreen

class MainActivity : ComponentActivity() {
    private lateinit var userPreferencesRepository: UserPreferencesRepository
    private lateinit var dataSyncRepository: DataSyncRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userPreferencesRepository = UserPreferencesRepository(this)
        dataSyncRepository = DataSyncRepository(this)
        setContent {
            val navController = rememberNavController()
            ShowerTimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "launcher") {
                        composable("launcher") {
                            val userPreferences by userPreferencesRepository.userPreferencesFlow.collectAsState(
                                initial = UserPreferences(HairLength.MI_LONGS, AlertPreference.VIBREUR_ET_SON, false)
                            )
                            if (userPreferences.isConfigured) {
                                navController.navigate("program_list") {
                                    popUpTo("launcher") { inclusive = true }
                                }
                            } else {
                                navController.navigate("settings") {
                                    popUpTo("launcher") { inclusive = true }
                                }
                            }
                        }
                        composable("settings") {
                            SettingsScreen(userPreferencesRepository = userPreferencesRepository)
                        }
                        composable("program_list") {
                            ProgramListScreen(
                                repository = userPreferencesRepository,
                                onProgramClick = { programId ->
                                    navController.navigate("timer/$programId")
                                },
                                onAddProgram = {
                                    navController.navigate("editor")
                                },
                                onEditProgram = { programId ->
                                    navController.navigate("editor?programId=$programId")
                                }
                            )
                        }
                        composable(
                            "timer/{programId}",
                            arguments = listOf(navArgument("programId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            TimerScreen(
                                programId = backStackEntry.arguments?.getString("programId") ?: "",
                                repository = userPreferencesRepository,
                                onNavigateToSummary = { totalTime ->
                                    navController.navigate("summary/$totalTime") {
                                        popUpTo("program_list")
                                    }
                                }
                            )
                        }
                        composable(
                            "summary/{totalTime}",
                            arguments = listOf(navArgument("totalTime") { type = NavType.IntType })
                        ) { backStackEntry ->
                            SummaryScreen(
                                totalTimeInSeconds = backStackEntry.arguments?.getInt("totalTime") ?: 0,
                                onFinish = { navController.popBackStack() }
                            )
                        }
                        composable(
                            "editor?programId={programId}",
                            arguments = listOf(navArgument("programId") {
                                type = NavType.StringType
                                nullable = true
                            })
                        ) { backStackEntry ->
                            ProgramEditorScreen(
                                programId = backStackEntry.arguments?.getString("programId"),
                                repository = userPreferencesRepository,
                                dataSyncRepository = dataSyncRepository,
                                onSave = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}