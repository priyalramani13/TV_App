package com.demo.androidtv

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.demo.androidtv.presentation.HomeViewModel
import com.demo.androidtv.ui.theme.BgColor
import com.demo.androidtv.ui.theme.DisplayStyle
import com.demo.androidtv.ui.theme.LiveRed
import com.demo.androidtv.ui.theme.Surface2Color
import com.demo.androidtv.ui.theme.SurfaceColor
import com.demo.androidtv.ui.theme.TennisYellow
import com.demo.androidtv.ui.theme.TextPrimary
import com.demo.androidtv.ui.theme.TextSecondary

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreen(
    onMatchClick: (Boolean, String) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    // Observe the data from the ViewModel
    val liveMatches by viewModel.liveMatches.collectAsState()
    val upcomingMatches by viewModel.upcomingMatches.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {

        // ==================== TOP NAV ====================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(BgColor)
                .padding(horizontal = 40.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Clean, Text-Based Top Nav Logo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "TENNIS",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "CHANNEL",
                        color = TennisYellow,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 2.sp
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TopNavItem("Live", isSelected = true)
                    TopNavItem("Replays")
                    TopNavItem("Schedule")
                    TopNavItem("My Sports")
                    TopNavItem("Browse")
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavIconButton { Text("🔍", fontSize = 14.sp) }
                NavIconButton(isGradient = true) {
                    Text(
                        "MK",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // ==================== SCROLLING CONTENT ====================
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(top = 24.dp, bottom = 40.dp)
        ) {
            // HERO SECTION
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                        .padding(horizontal = 40.dp)
                ) {
                    // Right Side: Cinematic Dark Graphic
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(420.dp, 260.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Surface2Color, BgColor),
                                    radius = 600f
                                ),
                                RoundedCornerShape(12.dp)
                            )
                            .border(
                                1.dp,
                                TennisYellow.copy(alpha = 0.3f),
                                RoundedCornerShape(12.dp)
                            )
                    ) {
                        // Inner styling to look like a premium broadcast window
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(1.dp)
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f))
                                    ),
                                    RoundedCornerShape(12.dp)
                                )
                        )

                        // Abstract Court Lines (Subtle and minimal)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.1f),
                                    RoundedCornerShape(4.dp)
                                )
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.White.copy(alpha = 0.1f))
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(80.dp)
                                .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
                        )

                        // "LIVE" Pulse Indicator in the corner
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(LiveRed, CircleShape)
                            )
                        }
                    }

                    // Left Side: Hero Text and Focusable Buttons
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 40.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .background(LiveRed, RoundedCornerShape(4.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(Color.White, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "LIVE NOW",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "WIMBLEDON 2026\nCENTRE COURT",
                            style = DisplayStyle.copy(
                                fontSize = 42.sp,
                                color = TextPrimary,
                                lineHeight = 42.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🎾 Djokovic vs Alcaraz", color = TextSecondary, fontSize = 13.sp)
                            Text("  ·  ", color = TextSecondary)
                            Text(
                                "2 - 1",
                                color = TennisYellow,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 4.sp
                            )
                            Text("  ·  ", color = TextSecondary)
                            Text("Set 3 · 15-40", color = TextSecondary, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Surface(
                                onClick = { onMatchClick(false, "live") },
                                shape = ClickableSurfaceDefaults.shape(shape = RoundedCornerShape(6.dp)),
                                colors = ClickableSurfaceDefaults.colors(
                                    containerColor = TennisYellow,
                                    focusedContainerColor = Color.White,
                                    contentColor = BgColor,
                                    focusedContentColor = BgColor
                                )
                            ) {
                                Box(
                                    modifier = Modifier.padding(
                                        horizontal = 24.dp,
                                        vertical = 10.dp
                                    )
                                ) {
                                    Text(
                                        "▶ Watch Live",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Surface(
                                onClick = {},
                                shape = ClickableSurfaceDefaults.shape(shape = RoundedCornerShape(6.dp)),
                                colors = ClickableSurfaceDefaults.colors(
                                    containerColor = Color.White.copy(alpha = 0.1f),
                                    focusedContainerColor = Color.White,
                                    contentColor = Color.White,
                                    focusedContentColor = BgColor
                                ),
                                border = ClickableSurfaceDefaults.border(
                                    border = Border(
                                        border = BorderStroke(
                                            1.dp,
                                            Color.White.copy(alpha = 0.15f)
                                        )
                                    ),
                                    focusedBorder = Border(
                                        border = BorderStroke(
                                            2.dp,
                                            Color.White
                                        )
                                    )
                                )
                            ) {
                                Box(
                                    modifier = Modifier.padding(
                                        horizontal = 24.dp,
                                        vertical = 10.dp
                                    )
                                ) {
                                    Text(
                                        "ℹ More Info",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ==================== LIVE RAIL ====================
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "Live Now",
                            color = TextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "LIVE",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .background(LiveRed, RoundedCornerShape(3.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Surface(
                        onClick = {},
                        colors = ClickableSurfaceDefaults.colors(
                            containerColor = Color.Transparent,
                            focusedContainerColor = Color.White.copy(alpha = 0.1f)
                        ),
                        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            "See All 14 →",
                            color = TennisYellow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(liveMatches) { match ->
                        HtmlMatchCard(
                            onClick = { onMatchClick(match.shouldFail, match.streamType) },
                            isLive = match.isLive,
                            emoji = match.emoji,
                            sport = match.sport,
                            title = match.title,
                            meta = match.meta,
                            score = match.score,
                            bgGradient = match.bgGradient,
                            tag = match.tag,
                            tagColor = match.tagColor,
                            tagText = match.tagText
                        )
                    }
                }
            }

            // ==================== COMING UP TODAY RAIL ====================
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Coming Up Today",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        onClick = {},
                        colors = ClickableSurfaceDefaults.colors(
                            containerColor = Color.Transparent,
                            focusedContainerColor = Color.White.copy(alpha = 0.1f)
                        ),
                        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(4.dp))
                    ) {
                        Text(
                            "Full Schedule →",
                            color = TennisYellow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(upcomingMatches) { match ->
                        HtmlMatchCard(
                            onClick = { onMatchClick(match.shouldFail, match.streamType) },
                            isLive = match.isLive,
                            emoji = match.emoji,
                            sport = match.sport,
                            title = match.title,
                            meta = match.meta,
                            score = match.score,
                            bgGradient = match.bgGradient,
                            tag = match.tag,
                            tagColor = match.tagColor,
                            tagText = match.tagText
                        )
                    }
                }
            }
        }
    }
}

// ---------------- Helper Components ----------------

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TopNavItem(text: String, isSelected: Boolean = false) {
    Surface(
        onClick = {},
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(6.dp)),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (isSelected) Color.White.copy(alpha = 0.06f) else Color.Transparent,
            focusedContainerColor = Color.White.copy(alpha = 0.2f),
            contentColor = if (isSelected) TennisYellow else TextSecondary,
            focusedContentColor = Color.White
        )
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun NavIconButton(isGradient: Boolean = false, content: @Composable () -> Unit) {
    Surface(
        onClick = {},
        shape = ClickableSurfaceDefaults.shape(CircleShape),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (isGradient) TennisYellow else Color.Transparent,
            focusedContainerColor = Color.White,
            contentColor = if (isGradient) BgColor else Color.White,
            focusedContentColor = BgColor
        ),
        modifier = Modifier.size(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HtmlMatchCard(
    onClick: () -> Unit,
    isLive: Boolean,
    emoji: String,
    sport: String,
    title: String,
    meta: String,
    score: String,
    bgGradient: List<Color>,
    tag: String,
    tagColor: Color,
    tagText: Color
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(200.dp),
        colors = CardDefaults.colors(
            containerColor = SurfaceColor,
            focusedContainerColor = Surface2Color
        ),
        border = CardDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(
                    2.dp,
                    TennisYellow
                )
            )
        ),
        scale = CardDefaults.scale(focusedScale = 1.05f)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(Brush.linearGradient(bgGradient)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 32.sp)

                if (isLive) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                            .background(LiveRed, RoundedCornerShape(3.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .background(Color.White, CircleShape)
                        ); Spacer(Modifier.width(4.dp))
                        Text(
                            "LIVE",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                Text(
                    tag,
                    color = tagText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(tagColor, RoundedCornerShape(3.dp))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    sport,
                    color = TennisYellow,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    title,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 16.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        meta,
                        color = TextSecondary,
                        fontSize = 10.sp,
                        modifier = Modifier.weight(1f)
                    )
                    if (score.isNotEmpty()) {
                        Text(
                            score,
                            color = Color(0xFFFFD600),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}