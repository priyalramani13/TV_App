package com.demo.androidtv.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Tennis Channel Brand Colors
val BgColor = Color(0xFF040B16)      // Deep Navy Background
val SurfaceColor = Color(0xFF0D1B2A) // Elevated Navy
val Surface2Color = Color(0xFF1B263B) // Lighter Navy for focused cards

// Signature Accents
val TennisYellow = Color(0xFFD4F82A) // Optic/Tennis Ball Yellow for focus & primary buttons
val LiveRed = Color(0xFFFF1744)

val TextPrimary = Color(0xFFF1F5F9)
val TextSecondary = Color(0xFF94A3B8)

// Tennis Channel's logo is typically solid white on dark backgrounds
val LogoGradient = Brush.linearGradient(listOf(Color.White, Color(0xFFE2E8F0)))

val DisplayStyle = TextStyle(
    fontSize = 72.sp,
    fontWeight = FontWeight.Bold,
    letterSpacing = 2.sp
)