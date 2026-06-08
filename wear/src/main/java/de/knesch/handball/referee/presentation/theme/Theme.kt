package de.knesch.handball.referee.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.MaterialTheme

@Composable
fun HandballSchiedsrichterTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WearColorScheme,
        content = content
    )
}