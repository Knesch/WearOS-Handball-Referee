package de.knesch.handball.referee.presentation

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material3.AlertDialog
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import de.knesch.handball.referee.R
import de.knesch.handball.referee.presentation.theme.AmberHalftime
import de.knesch.handball.referee.presentation.theme.GreenRunning
import de.knesch.handball.referee.presentation.theme.RedStop

@Composable
fun MatchScreen(viewModel: MatchViewModel = viewModel()) {

    var showResetDialog by remember { mutableStateOf(false) }
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
                Text(stringResource(R.string.action_back), fontSize = 14.sp)
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
                Text(stringResource(R.string.action_halftime), fontSize = 14.sp)
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
                Text(stringResource(R.string.action_reset), fontSize = 14.sp)
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

    Box(
        modifier = (if (viewModel.useOngoingActivity) Modifier else Modifier.keepScreenOn())
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .weight(1.5f)
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // Stopwatch Display
                val minutes = (viewModel.elapsedMillis / 1000) / 60
                val seconds = (viewModel.elapsedMillis / 1000) % 60
                Text(
                    text = "%02d:%02d".format(minutes, seconds),
                    style = MaterialTheme.typography.displayLarge,
                    color = if (viewModel.isRunning) GreenRunning else Color.White,
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { viewModel.toggleStopWatch() }
                        )
                    }
                )
            }

            // Score Display
            Row(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScoreColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
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
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold
                )

                ScoreColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
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

            // Controls
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 10.dp, top = 6.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            )
            {
                Button(
                    onClick = { showMenu = true },
                    //modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(stringResource(R.string.menu_title), fontSize = 10.sp)
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
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onAdd() },
                    onLongPress = { onRemove() }
                )
            }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor.copy(alpha = 0.8f)
        )
        Text(
            text = score.toString(),
            style = MaterialTheme.typography.displayLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 50.sp,
            color = contentColor
        )
    }
}

@WearPreviewDevices
@Composable
fun MatchScreenPreview() {
    MatchScreen()
}
