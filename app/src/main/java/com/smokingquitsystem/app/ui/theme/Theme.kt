package com.smokingquitsystem.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Bg = Color(0xFF08130F)
val Panel = Color(0xFF12231B)
val Green = Color(0xFF35D07F)
val Soft = Color(0xFF9FB7A9)
val White = Color(0xFFF2F7F3)
val ReminderCardBg = Color(0xFF173626)
val SelectedRowBg = Color(0xFF1B3B2A)
val NavIndicatorBg = Color(0xFF244B36)
val BorderSoft = Color(0xFF345143)

private val AppDarkColorScheme = darkColorScheme(
    primary = Green,
    onPrimary = Bg,
    background = Bg,
    surface = Panel,
    onBackground = White,
    onSurface = White
)

@Composable
fun SmokingQuitTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppDarkColorScheme,
        content = content
    )
}
