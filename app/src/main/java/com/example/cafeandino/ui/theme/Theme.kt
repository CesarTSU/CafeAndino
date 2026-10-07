package com.example.cafeandino.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = CoffeePrimary,
    onPrimary = CoffeeOnPrimary,
    primaryContainer = CoffeePrimaryContainer,
    onPrimaryContainer = CoffeeOnPrimaryContainer,
    secondary = CoffeeSecondary,
    onSecondary = CoffeeOnSecondary,
    background = CoffeeDarkBackground,
    onBackground = CoffeeOnBackground,
    surface = CoffeeDarkSurface,
    onSurface = CoffeeOnSurface,
    surfaceVariant = CoffeeDarkSurfaceVariant,
    onSurfaceVariant = CoffeeOnSurfaceVariant
)

@Composable
fun CafeAndinoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
