package de.knesch.handball.referee.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import de.knesch.handball.referee.R

@Composable
fun StartScreen(
    onNewGameClick: () -> Unit,
    onConfigClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(de.knesch.handball.referee.shared.R.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = onNewGameClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.to_the_game))
            }

            Button(
                onClick = onConfigClick,
                modifier = Modifier.fillMaxWidth()
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
