/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.quvntvn.showertime.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.quvntvn.showertime.wear.presentation.theme.ShowerTimeTheme
import com.quvntvn.showertime.wear.ui.programs.ProgramListScreen
import com.quvntvn.showertime.wear.ui.timer.TimerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            val navController = rememberSwipeDismissableNavController()
            ShowerTimeTheme {
                SwipeDismissableNavHost(
                    navController = navController,
                    startDestination = "program_list"
                ) {
                    composable("program_list") {
                        ProgramListScreen(
                            onProgramClick = { programId ->
                                navController.navigate("timer/$programId")
                            }
                        )
                    }
                    composable(
                        "timer/{programId}",
                        arguments = listOf(navArgument("programId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        TimerScreen(
                            programId = backStackEntry.arguments?.getString("programId") ?: "",
                            onFinish = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}