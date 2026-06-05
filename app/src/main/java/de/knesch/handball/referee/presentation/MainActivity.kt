package de.knesch.handball.referee.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import de.knesch.handball.referee.presentation.theme.HandballSchiedsrichterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HandballSchiedsrichterApp()
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

@Composable
fun HandballSchiedsrichterApp() {
    val navController = rememberSwipeDismissableNavController()
    val viewModel: MatchViewModel = viewModel()
    HandballSchiedsrichterTheme {
        AppScaffold {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "start"
            ) {
                composable("start") {
                    StartScreen(
                        onNewGameClick = { navController.navigate("match") },
                        onConfigClick = { navController.navigate("config") }
                    )
                }
                composable("match") {
                    MatchScreen(viewModel = viewModel)
                }
                composable("config") {
                    ConfigScreen(
                        viewModel = viewModel,
                        onBackClick = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun DefaultPreview() {
    HandballSchiedsrichterApp()
}
