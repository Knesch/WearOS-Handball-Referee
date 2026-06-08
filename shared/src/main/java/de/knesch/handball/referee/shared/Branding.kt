package de.knesch.handball.referee.shared

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun AppBranding(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.app_name),
        style = MaterialTheme.typography.headlineMedium,
        modifier = modifier
    )
}
