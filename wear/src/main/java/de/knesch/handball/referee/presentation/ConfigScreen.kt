package de.knesch.handball.referee.presentation

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.AlertDialog
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import de.knesch.handball.referee.R
import kotlin.system.exitProcess

@Composable
fun ConfigScreen(
    viewModel: MatchViewModel,
    onBackClick: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    val context = LocalContext.current
    var showRestartDialog by remember { mutableStateOf(false) }

    val colors = listOf(
        Color.White, Color.Black, Color.Red, Color.Blue,
        Color.Yellow, Color.Green, Color(0xFFFFA500), // Orange
        Color(0xFF800080) // Purple
    )

    AlertDialog(
        visible = showRestartDialog,
        onDismissRequest = { showRestartDialog = false },
        confirmButton = {
            Button(
                onClick = {
                    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    exitProcess(0)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(R.string.dialog_yes))
            }
        },
        dismissButton = {
            Button(
                onClick = { showRestartDialog = false }
            ) {
                Text(stringResource(R.string.dialog_no))
            }
        },
        title = { Text(stringResource(R.string.restart_dialog_title)) },
        text = { Text(stringResource(R.string.restart_dialog_message)) }
    )

    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ListHeader {
                Text(stringResource(R.string.start_config))
            }
        }

        item {
            ColorSelector(
                label = stringResource(R.string.team_home),
                currentColor = viewModel.colorHeim,
                onColorChange = { viewModel.colorHeim = it },
                availableColors = colors
            )
        }

        item {
            ColorSelector(
                label = stringResource(R.string.team_guest),
                currentColor = viewModel.colorGast,
                onColorChange = { viewModel.colorGast = it },
                availableColors = colors
            )
        }

        item {
            ListHeader {
                Text(stringResource(R.string.display_mode), modifier = Modifier.padding(top = 8.dp))
            }
        }

        item {
            Button(
                onClick = {
                    viewModel.updateOngoingActivity(!viewModel.useOngoingActivity)
                    showRestartDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (viewModel.useOngoingActivity) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    if (viewModel.useOngoingActivity) stringResource(R.string.display_mode_ongoing)
                    else stringResource(R.string.display_mode_always_on)
                )
            }
        }

        item {
            Button(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(stringResource(R.string.action_back))
            }
        }
    }
}

@Composable
fun ColorSelector(
    label: String,
    currentColor: Color,
    onColorChange: (Color) -> Unit,
    availableColors: List<Color>
) {
    val currentIndex = availableColors.indexOf(currentColor).coerceAtLeast(0)

    Button(
        onClick = {
            val nextIndex = (currentIndex + 1) % availableColors.size
            onColorChange(availableColors[nextIndex])
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = currentColor,
            contentColor = if (isDark(currentColor)) Color.White else Color.Black
        )
    ) {
        Text("$label Trikot")
    }
}
