package de.knesch.handball.referee.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import de.knesch.handball.referee.R

@Composable
fun StartScreen(
    onNewGameClick: () -> Unit,
    onConfigClick: () -> Unit,
    listState: ScalingLazyListState = rememberScalingLazyListState()
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            ListHeader {
                Text(
                    text = stringResource(de.knesch.handball.referee.shared.R.string.app_name),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        item {
            Button(
                onClick = onNewGameClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(stringResource(R.string.to_the_game))
            }
        }

        item {
            Button(
                onClick = onConfigClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(stringResource(R.string.start_config))
            }
        }
    }
}

@WearPreviewDevices
@Composable
fun StartScreenPreview() {
    StartScreen(onNewGameClick = {}, onConfigClick = {})
}
