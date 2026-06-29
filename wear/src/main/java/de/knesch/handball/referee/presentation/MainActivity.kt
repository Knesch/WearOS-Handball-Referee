package de.knesch.handball.referee.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import de.knesch.handball.referee.R
import de.knesch.handball.referee.presentation.theme.HandballSchiedsrichterTheme

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("HandballReferee", "Notification permission granted")
        } else {
            Log.d("HandballReferee", "Notification permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("handball_prefs", MODE_PRIVATE)
        val useOngoingActivity = prefs.getBoolean("use_ongoing_activity", false)
        Log.i("HandballReferee", "MainActivity onCreate, useOngoingActivity=$useOngoingActivity")

        if (useOngoingActivity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        // Capability-Check erzwingen
        try {
            val caps = resources.getStringArray(R.array.android_wear_capabilities)
            Log.i("HandballReferee", "Wear-App gestartet. Capabilities: ${caps.joinToString()}")
        } catch (e: Exception) {
            Log.e("HandballReferee", "FEHLER: Capabilities konnten nicht geladen werden!", e)
        }

        setContent {
            HandballSchiedsrichterApp()
        }

        if (!useOngoingActivity) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
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
