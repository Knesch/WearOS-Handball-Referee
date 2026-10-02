package de.knesch.handball.referee.presentation

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.AlertDialog
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.Dialog
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Picker
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.rememberPickerState
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import de.knesch.handball.referee.R
import de.knesch.handball.referee.presentation.theme.AmberHalftime
import de.knesch.handball.referee.presentation.theme.GreenRunning
import de.knesch.handball.referee.presentation.theme.RedStop

@Composable
fun MatchScreen(
    viewModel: MatchViewModel = viewModel(),
    listState: ScalingLazyListState = rememberScalingLazyListState()
) {

    var showResetDialog by remember { mutableStateOf(false) }
    var showTimeAdjustDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    // Reset Confirmation Dialog
    AlertDialog(
        visible = showResetDialog,
        onDismissRequest = { showResetDialog = false },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.reset()
                    showResetDialog = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(R.string.dialog_yes))
            }
        },
        dismissButton = {
            Button(
                onClick = { showResetDialog = false }
            ) {
                Text(stringResource(R.string.dialog_no))
            }
        },
        title = { Text(stringResource(R.string.dialog_reset_title)) },
        text = { Text(stringResource(R.string.dialog_reset_message)) }
    )

    // Time Adjustment Dialog
    TimeAdjustDialog(
        visible = showTimeAdjustDialog,
        onDismissRequest = { showTimeAdjustDialog = false },
        initialElapsedMillis = viewModel.elapsedMillis,
        onConfirm = { newMillis ->
            viewModel.updateElapsedMillis(newMillis)
            showTimeAdjustDialog = false
        }
    )

    // Menu Dialog
    AlertDialog(
        visible = showMenu,
        onDismissRequest = { showMenu = false },
        title = { Text(stringResource(R.string.menu_title)) }
    ) {
        item {
            Button(
                onClick = {
                    showMenu = false
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.action_back))
            }
        }
        item {
            Button(
                onClick = {
                    viewModel.halftime()
                    showMenu = false
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = AmberHalftime)
            ) {
                Text(stringResource(R.string.action_halftime))
            }
        }
        item {
            Button(
                onClick = {
                    showMenu = false
                    showResetDialog = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RedStop,
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(R.string.action_reset))
            }
        }
    }

    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Vibrator::class.java) }

    fun vibrateShort() {
        vibrator?.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    fun vibrateLong() {
        vibrator?.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    ScalingLazyColumn(
        modifier = (if (viewModel.useOngoingActivity) Modifier else Modifier.keepScreenOn())
            .fillMaxSize(),
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Stopwatch Display
        item {
            val minutes = (viewModel.elapsedMillis / 1000) / 60
            val seconds = (viewModel.elapsedMillis / 1000) % 60
            Text(
                text = "%02d:%02d".format(minutes, seconds),
                style = MaterialTheme.typography.displayMedium,
                color = if (viewModel.isRunning) GreenRunning else Color.White,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { viewModel.toggleStopWatch() },
                            onLongPress = {
                                if (!viewModel.isRunning) {
                                    vibrateShort()
                                    showTimeAdjustDialog = true
                                }
                            }
                        )
                    }
            )
        }

        // Score Display
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScoreColumn(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.team_home),
                    score = viewModel.toreHeim,
                    color = viewModel.colorHeim,
                    onAdd = {
                        viewModel.addTorHeim()
                        vibrateShort()
                    },
                    onRemove = {
                        viewModel.removeTorHeim()
                        vibrateLong()
                    }
                )

                Text(
                    text = ":",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                ScoreColumn(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.team_guest),
                    score = viewModel.toreGast,
                    color = viewModel.colorGast,
                    onAdd = {
                        viewModel.addTorGast()
                        vibrateShort()
                    },
                    onRemove = {
                        viewModel.removeTorGast()
                        vibrateLong()
                    }
                )
            }
        }

        // Controls
        item {
            Button(
                onClick = { showMenu = true },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(stringResource(R.string.menu_title))
            }
        }
    }
}

@Composable
fun TimeAdjustDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    initialElapsedMillis: Long,
    onConfirm: (Long) -> Unit
) {
    if (!visible) return

    val initialMinutes = ((initialElapsedMillis / 1000) / 60).toInt().coerceIn(0, 30)
    val initialSeconds = ((initialElapsedMillis / 1000) % 60).toInt().coerceIn(0, 59)

    val minuteState = rememberPickerState(
        initialNumberOfOptions = 31,
        initiallySelectedIndex = initialMinutes
    )
    val secondState = rememberPickerState(
        initialNumberOfOptions = 60,
        initiallySelectedIndex = initialSeconds
    )

    Dialog(
        visible = visible,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.dialog_adjust_time_title),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 4.dp)
            )

            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Picker(
                    state = minuteState,
                    contentDescription = { "Minutes" },
                    modifier = Modifier
                        .width(52.dp)
                        .fillMaxHeight()
                ) { optionIndex ->
                    val isSelected = optionIndex == selectedOptionIndex
                    Text(
                        text = "%02d".format(optionIndex),
                        style = MaterialTheme.typography.displaySmall,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }

                Text(
                    text = ":",
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )

                Picker(
                    state = secondState,
                    contentDescription = { "Seconds" },
                    modifier = Modifier
                        .width(52.dp)
                        .fillMaxHeight()
                ) { optionIndex ->
                    val isSelected = optionIndex == selectedOptionIndex
                    Text(
                        text = "%02d".format(optionIndex),
                        style = MaterialTheme.typography.displaySmall,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onDismissRequest,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RedStop,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.dialog_no)
                    )
                }

                Button(
                    onClick = {
                        val newMillis = (minuteState.selectedOptionIndex * 60L + secondState.selectedOptionIndex * 1L) * 1000L
                        onConfirm(newMillis)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GreenRunning,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.dialog_yes)
                    )
                }
            }
        }
    }
}

@Composable
fun ScoreColumn(
    modifier: Modifier = Modifier,
    label: String,
    score: Int,
    color: Color,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    val contentColor = if (isDark(color)) Color.White else Color.Black

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .background(color)
            .padding(vertical = 8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onAdd() },
                    onLongPress = { onRemove() }
                )
            }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor.copy(alpha = 0.8f)
        )
        Text(
            text = score.toString(),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
    }
}

@WearPreviewDevices
@Composable
fun MatchScreenPreview() {
    MatchScreen()
}
