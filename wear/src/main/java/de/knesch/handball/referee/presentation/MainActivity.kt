package de.knesch.handball.referee.presentation

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.KeyEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.TimeText
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import androidx.wear.compose.ui.tooling.preview.WearPreviewFontScales
import androidx.wear.input.WearableButtons
import de.knesch.handball.referee.R
import de.knesch.handball.referee.presentation.theme.HandballSchiedsrichterTheme

tailrec fun Context.findActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

class MainActivity : ComponentActivity() {

    var currentRoute: String = "start"
    private lateinit var matchViewModel: MatchViewModel

    private fun isActionKey(keyCode: Int): Boolean = when (keyCode) {
        KeyEvent.KEYCODE_VOLUME_UP,
        KeyEvent.KEYCODE_VOLUME_DOWN -> true
        else -> false
    }

    @Suppress("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (isActionKey(event.keyCode) && (currentRoute == "match")) {
            Log.d("HandballReferee", "Action key intercepted: keyCode=${event.keyCode}, action=${event.action}")
            if ((event.action == KeyEvent.ACTION_DOWN) && (event.repeatCount == 0)) {
                matchViewModel.toggleStopWatch()
                val vibrator = getSystemService(Vibrator::class.java)
                vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
            }
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return (isActionKey(keyCode) && (currentRoute == "match")) || super.onKeyDown(keyCode, event)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("HandballReferee", "Notification permission granted")
        } else {
            Log.d("HandballReferee", "Notification permission denied")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        matchViewModel = ViewModelProvider(this)[MatchViewModel::class.java]

        val prefs = getSharedPreferences("handball_prefs", MODE_PRIVATE)
        val useOngoingActivity = prefs.getBoolean("use_ongoing_activity", false)
        Log.i("HandballReferee", "MainActivity onCreate, useOngoingActivity=$useOngoingActivity")

        if (useOngoingActivity && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        try {
            val caps = resources.getStringArray(R.array.android_wear_capabilities)
            Log.i("HandballReferee", "Wear-App gestartet. Capabilities: ${caps.joinToString()}")

            val buttonCount = WearableButtons.getButtonCount(this)
            Log.i("HandballReferee", "WearableButtons count: $buttonCount")
        } catch (e: Exception) {
            Log.e("HandballReferee", "FEHLER beim Auslesen der Tasten / Capabilities!", e)
        }

        val startTarget = intent.getStringExtra("target")
        Log.i("HandballReferee", "MainActivity started with target: $startTarget")

        setContent {
            HandballSchiedsrichterApp(startTarget, matchViewModel)
        }

        if (!useOngoingActivity) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

@Composable
fun HandballSchiedsrichterApp(
    startTarget: String?,
    viewModel: MatchViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    val navController = rememberSwipeDismissableNavController()
    val context = LocalContext.current
    val activity = context.findActivity() as? MainActivity
    val initialRoute = if (startTarget == "match") "match" else "start"

    DisposableEffect(activity, navController) {
        fun updateRoute(route: String?) {
            val targetRoute = route ?: initialRoute
            activity?.currentRoute = targetRoute
            Log.i("HandballReferee", "Current route updated to: $targetRoute")
        }

        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            updateRoute(destination.route)
        }
        navController.addOnDestinationChangedListener(listener)

        updateRoute(navController.currentDestination?.route ?: initialRoute)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    HandballSchiedsrichterTheme {
        AppScaffold {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = initialRoute,
            ) {
                composable("start") {
                    val listState = rememberScalingLazyListState()
                    ScreenScaffold(
                        scrollState = listState,
                        timeText = { TimeText() }
                    ) {
                        StartScreen(
                            onNewGameClick = { navController.navigate("match") },
                            onConfigClick = { navController.navigate("config") },
                            listState = listState
                        )
                    }
                }
                composable("match") {
                    val listState = rememberScalingLazyListState()
                    ScreenScaffold(
                        scrollState = listState,
                        timeText = { TimeText() }
                    ) {
                        MatchScreen(
                            viewModel = viewModel,
                            listState = listState
                        )
                    }
                }
                composable("config") {
                    val listState = rememberScalingLazyListState()
                    ScreenScaffold(
                        scrollState = listState,
                        timeText = { TimeText() }
                    ) {
                        ConfigScreen(
                            viewModel = viewModel,
                            onBackClick = { navController.popBackStack() },
                            listState = listState
                        )
                    }
                }
            }
        }
    }
}

@WearPreviewDevices
@WearPreviewFontScales
@Composable
fun DefaultPreview() {
    HandballSchiedsrichterApp(null)
}
