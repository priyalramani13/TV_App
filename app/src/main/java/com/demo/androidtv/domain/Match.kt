package com.demo.androidtv.domain

import androidx.compose.ui.graphics.Color

data class Match(
    val id: String,
    val sport: String,
    val title: String,
    val meta: String,
    val score: String = "",
    val emoji: String,
    val isLive: Boolean,
    val tag: String,
    val tagColor: Color,
    val tagText: Color,
    val bgGradient: List<Color>,
    val streamType: String,
    val shouldFail: Boolean = false
)