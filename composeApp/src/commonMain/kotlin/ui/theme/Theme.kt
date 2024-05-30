package ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores para tema Light
val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE), // Azul primario
    secondary = Color(0xFF3700B3), // Azul secundario
    background = Color.White, // Blanco de fondo
    surface = Color.White, // Blanco de superficie
    onSurface = Color.Black, // Negro sobre superficie
    onPrimaryContainer = Color.Black, // Negro de texto
    error = Color(0xFFB00020), // Rojo error
)

// Colores para tema Dark
val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF3700B3), // Azul primario (más oscuro)
    secondary = Color(0xFF2196F3), // Azul secundario (más brillante)
    background = Color(0xFF303030), // Gris oscuro de fondo
    surface = Color(0xFF202020), // Gris oscuro de superficie
    onBackground = Color.White, // Blanco sobre fondo
    onSurface = Color.White, // Blanco sobre superficie
    onPrimaryContainer = Color.White, // Blanco de texto
    error = Color(0xFFEF5350), // Rojo error (más brillante)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
