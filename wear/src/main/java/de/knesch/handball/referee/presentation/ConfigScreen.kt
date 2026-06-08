package de.knesch.handball.referee.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.Text
import de.knesch.handball.referee.R

@Composable
fun ConfigScreen(
    viewModel: MatchViewModel,
    onBackClick: () -> Unit
) {
    val listState = rememberScalingLazyListState()
    val colors = listOf(
        Color.White, Color.Black, Color.Red, Color.Blue,
        Color.Yellow, Color.Green, Color(0xFFFFA500), // Orange
        Color(0xFF800080) // Purple
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
