package it.unipd.dei.jetpack_compose_frontend.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun EconomyTrackerTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(colorScheme = ColorScheme.)(
        content = content
    )
}