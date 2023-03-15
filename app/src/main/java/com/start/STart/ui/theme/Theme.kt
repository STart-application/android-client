package com.start.STart.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Shapes
import androidx.compose.runtime.Composable

@Composable
fun DreamTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}