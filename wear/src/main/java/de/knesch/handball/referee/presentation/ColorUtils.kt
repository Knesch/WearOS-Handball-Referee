package de.knesch.handball.referee.presentation

import androidx.compose.ui.graphics.Color

fun isDark(color: Color): Boolean {
    val luminance = 0.299 * color.red + 0.587 * color.green + 0.114 * color.blue
    return luminance < 0.5
}
