package com.example.setupsheets.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Defines the typography styles used throughout the app.
 *
 * This file configures the default text appearance (font family, size, weight, etc.)
 * for different text components in the Compose UI. Adjusting these styles allows
 * for consistent branding and readability across the app.
 */
val Typography = Typography(
    /**
     * bodyLarge — the default body text style.
     * Used for the main content text displayed throughout the app
     */
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),

    /**
     * titleLarge — a large title text style.
     * Used for section headings or screen titles to establish hierarchy
     */
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),

    /**
     * titleMedium — a medium title text style.
     * Used for card titles, buttons, and small section headings
     */
    titleMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
    ),

    /**
     * headlineLarge — a prominent headline style.
     * Used for key titles or screens where emphasis is required
     */
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 26.sp,
    )
)
