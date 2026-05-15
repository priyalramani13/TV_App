package com.demo.androidtv

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.demo.androidtv.presentation.PlayerViewModel
import com.demo.androidtv.ui.theme.BgColor
import com.demo.androidtv.ui.theme.LiveRed
import com.demo.androidtv.ui.theme.Surface2Color
import com.demo.androidtv.ui.theme.SurfaceColor
import com.demo.androidtv.ui.theme.TennisYellow
import com.demo.androidtv.ui.theme.TextPrimary
import com.demo.androidtv.ui.theme.TextSecondary

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(UnstableApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun PlayerScreen(
    shouldFail: Boolean,
    streamType: String = "live",
    onBackPress: () -> Unit,
    viewModel: PlayerViewModel = viewModel() // Inject ViewModel
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState() // Observe State

    // 1. Tell ViewModel to prepare the stream data when screen opens
    LaunchedEffect(shouldFail, streamType) {
        viewModel.loadStream(shouldFail, streamType)
    }

    // 2. Build ExoPlayer (UI Layer)
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    viewModel.setBuffering(playbackState == Player.STATE_BUFFERING)
                    if (playbackState == Player.STATE_ENDED) {
                        viewModel.triggerEndScreen()
                    }
                }
            })
        }
    }

    // 3. React to ViewModel state changes
    LaunchedEffect(state.streamUrl) {
        if (state.streamUrl.isNotEmpty()) {
            exoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(state.streamUrl)))
            exoPlayer.prepare()
            if (!shouldFail) exoPlayer.playWhenReady = true
        }
    }

    LaunchedEffect(state.showError) {
        if (state.showError) exoPlayer.stop() // Stop playing if error triggers
    }

    BackHandler {
        exoPlayer.release()
        onBackPress()
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    // ==================== UI RENDERING ====================
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {

        // Video Player Surface
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Player Controls Overlay
        if (!state.showError && !state.showEndScreen) {
            PlayerOverlay(
                isPlaying = state.isPlaying,
                onPlayPauseClick = {
                    viewModel.togglePlayPause()
                    if (state.isPlaying) exoPlayer.pause() else exoPlayer.play()
                },
                onBackClick = {
                    exoPlayer.release()
                    onBackPress()
                }
            )
        }

        // Loading Spinner
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = TennisYellow,
                    modifier = Modifier.size(64.dp),
                    strokeWidth = 6.dp
                )
            }
        }

        // Error Overlay
        if (state.showError) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.92f)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("⚠️", fontSize = 48.sp)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Playback Error",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "We encountered an issue loading this stream. Our team has\nbeen automatically notified. Please try again or contact\nsupport.",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                    Spacer(Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .background(SurfaceColor, RoundedCornerShape(8.dp))
                            .border(
                                1.dp,
                                Color(0xFFFF3D5A).copy(alpha = 0.3f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "ERROR ID: SSPTV-2026-E403-DRM-WIDEVINE",
                                color = Color(0xFFFF3D5A),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 1.sp
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Reference this ID when contacting support",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Retry Button
                        Surface(
                            onClick = { viewModel.triggerErrorSequence() }, // Handled purely by ViewModel now!
                            shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp)),
                            colors = ClickableSurfaceDefaults.colors(
                                containerColor = TennisYellow,
                                focusedContainerColor = Color.White,
                                contentColor = BgColor,
                                focusedContentColor = BgColor
                            )
                        ) {
                            Text(
                                "↺ Retry Stream",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                            )
                        }

                        // Go Back Button
                        Surface(
                            onClick = { exoPlayer.release(); onBackPress() },
                            shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp)),
                            colors = ClickableSurfaceDefaults.colors(
                                containerColor = SurfaceColor,
                                focusedContainerColor = Color.White.copy(alpha = 0.2f)
                            ),
                            border = ClickableSurfaceDefaults.border(
                                border = Border(
                                    BorderStroke(
                                        1.dp,
                                        Color.White.copy(alpha = 0.1f)
                                    )
                                ), focusedBorder = Border(BorderStroke(2.dp, Color.White))
                            )
                        ) {
                            Text(
                                "Go Back",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                            )
                        }
                    }
                }
            }
        }

        // End of Stream Overlay
        if (state.showEndScreen) {
            StreamEndedOverlay(
                onHomeClick = {
                    exoPlayer.release()
                    onBackPress()
                },
                onReplayClick = {
                    viewModel.resetEndScreen() // Reset UI via ViewModel
                    exoPlayer.seekTo(0)
                    exoPlayer.play()
                }
            )
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun StreamEndedOverlay(onHomeClick: () -> Unit, onReplayClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.92f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "MATCH OVER",
                color = TextSecondary,
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "DJOKOVIC WINS!",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Up Next on Tennis Channel", // Updated branding!
                color = TennisYellow,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Recommendation Cards
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                UpNextCard(
                    emoji = "⚽",
                    title = "Man City vs Arsenal",
                    subtitle = "Premier League · Live",
                    bgGradient = listOf(Color(0xFF2A0A3A), Color(0xFF4A1060)),
                    onClick = onReplayClick
                )
                UpNextCard(
                    emoji = "🏀",
                    title = "NBA Playoffs G5",
                    subtitle = "Lakers vs Celtics",
                    bgGradient = listOf(Color(0xFF0A2A4A), Color(0xFF0D3A6E)),
                    onClick = onReplayClick
                )
                UpNextCard(
                    emoji = "🎾",
                    title = "Wimbledon QF Replay",
                    subtitle = "On Demand",
                    bgGradient = listOf(Color(0xFF1A3A0A), Color(0xFF2A5A10)),
                    onClick = onReplayClick
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Return to Home Button
            Surface(
                onClick = onHomeClick,
                shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp)),
                colors = ClickableSurfaceDefaults.colors(
                    containerColor = SurfaceColor,
                    focusedContainerColor = Color.White,
                    contentColor = Color.White,
                    focusedContentColor = BgColor
                ),
                border = ClickableSurfaceDefaults.border(
                    border = Border(
                        BorderStroke(
                            1.dp,
                            Color.White.copy(alpha = 0.2f)
                        )
                    ),
                    focusedBorder = Border(
                        BorderStroke(
                            2.dp,
                            TennisYellow
                        )
                    )
                )
            ) {
                Text(
                    "Return to Home",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun UpNextCard(
    emoji: String,
    title: String,
    subtitle: String,
    bgGradient: List<Color>,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(8.dp)),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = SurfaceColor,
            focusedContainerColor = Surface2Color
        ),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(
                BorderStroke(
                    2.dp,
                    TennisYellow
                )
            )
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.05f),
        modifier = Modifier.size(160.dp, 120.dp)
    ) {
        Column {
            // Top half: Gradient with Emoji
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Brush.linearGradient(bgGradient)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 32.sp)
            }
            // Bottom half: Metadata
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(BgColor)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    title,
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Text(subtitle, color = TextSecondary, fontSize = 9.sp, maxLines = 1)
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PlayerOverlay(
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onBackClick: () -> Unit
) {
    // This Box floats on top of the AndroidView(ExoPlayer)
    Box(modifier = Modifier.fillMaxSize()) {

        // ==================== TOP BAR ====================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Black.copy(alpha = 0.8f),
                            Color.Transparent
                        )
                    )
                )
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Wimbledon 2026 · Centre Court",
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "🎾 Djokovic vs Alcaraz · Set 3 · 15-40 ·",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("🌍 London, UK", color = TextSecondary, fontSize = 11.sp)
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Widevine Badge
                Row(
                    modifier = Modifier
                        .background(
                            Color(0xFFCE93D8).copy(alpha = 0.1f),
                            RoundedCornerShape(4.dp)
                        )
                        .border(
                            1.dp,
                            Color(0xFFCE93D8).copy(alpha = 0.3f),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "🔒 WIDEVINE",
                        color = Color(0xFFCE93D8),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // LIVE Badge
                Row(
                    modifier = Modifier
                        .background(LiveRed, RoundedCornerShape(4.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color.White, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "LIVE",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                // GO LIVE Button
                Surface(
                    onClick = {},
                    shape = ClickableSurfaceDefaults.shape(RoundedCornerShape(4.dp)),
                    colors = ClickableSurfaceDefaults.colors(
                        containerColor = LiveRed.copy(alpha = 0.2f),
                        focusedContainerColor = LiveRed.copy(alpha = 0.4f)
                    ),

                    border = ClickableSurfaceDefaults.border(
                        border = Border(
                            border = BorderStroke(
                                1.dp,
                                LiveRed
                            )
                        ),
                        focusedBorder = Border(border = BorderStroke(2.dp, Color.White))
                    )
                ) {
                    Text(
                        "⚡ GO LIVE",
                        color = LiveRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }
        }

        // ==================== BOTTOM BAR ====================
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.9f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            // Scrub Bar Text
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
//                Text(
//                    "▶ LIVE — 47:32 behind",
//                    color = TennisYellow,
//                    fontSize = 10.sp
//                ) // Matching the blue text from screenshot
//                Text("Live Edge ●", color = TextSecondary, fontSize = 10.sp)
            }
//            Spacer(modifier = Modifier.height(6.dp))

            // Scrub Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(2.dp))
            ) {
                // Buffer bar (faint white)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .fillMaxHeight()
                        .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
                )

                // Progress bar (blue)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.65f)
                        .fillMaxHeight()
                        .background(TennisYellow, RoundedCornerShape(2.dp))
                )

                // Live Marker
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(4.dp)
                        .background(Color.White, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Controls
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PlayerButton("✕", onClick = onBackClick)
                    PlayerButton("⊞")
                }

                // Center Playback Controls
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayerButton("⟨⟨ 30")

                    // Main Play/Pause Button
                    Surface(
                        onClick = onPlayPauseClick,
                        modifier = Modifier.size(48.dp),
                        shape = ClickableSurfaceDefaults.shape(CircleShape),
                        colors = ClickableSurfaceDefaults.colors(
                            containerColor = TennisYellow,
                            focusedContainerColor = Color.White,
                            pressedContainerColor = Color.White,
                            contentColor = BgColor,
                            focusedContentColor = BgColor
                        ),
                        border = ClickableSurfaceDefaults.border(
                            focusedBorder = Border(
                                border = BorderStroke(
                                    2.dp,
                                    TennisYellow
                                )
                            )
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Drawing the icons manually with BgColor (Dark Navy) so they show up on the Yellow/White button
                            if (isPlaying) {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .width(4.dp)
                                            .height(16.dp)
                                            .background(BgColor)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .width(4.dp)
                                            .height(16.dp)
                                            .background(BgColor)
                                    )
                                }
                            } else {
                                androidx.compose.foundation.Canvas(modifier = Modifier.size(16.dp)) {
                                    val path = androidx.compose.ui.graphics.Path().apply {
                                        moveTo(0f, 0f)
                                        lineTo(size.width, size.height / 2f)
                                        lineTo(0f, size.height)
                                        close()
                                    }
                                    drawPath(path, BgColor) // Changed from Color.White to BgColor
                                }
                            }
                        }
                    }

                    PlayerButton("30 ⟩⟩")
                }

                // Right Controls
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlayerButton("🔊")

                    // Mock Volume Bar
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(2.dp)
                            .background(Color.White.copy(alpha = 0.3f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .fillMaxHeight()
                                .background(Color.White)
                        )
                    }

                    PlayerButton("CC", isActive = true)
                    PlayerButton("🎧")

                    // Spoiler Toggle Mock
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .height(16.dp)
                                .background(Color(0xFF172035), RoundedCornerShape(8.dp))
                                .border(
                                    1.dp,
                                    Color.White.copy(alpha = 0.1f),
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(2.dp)
                                    .size(12.dp)
                                    .background(Color.White, CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Spoiler", color = TextSecondary, fontSize = 10.sp)
                    }

                    PlayerButton("DEV")
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PlayerButton(text: String, isActive: Boolean = false, onClick: () -> Unit = {}) {
    Surface(
        onClick = onClick,
        modifier = Modifier.size(38.dp),
        shape = ClickableSurfaceDefaults.shape(shape = CircleShape),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (isActive) TennisYellow else Color.White.copy(alpha = 0.1f),
            focusedContainerColor = Color.White,
            pressedContainerColor = Color.White,
            contentColor = if (isActive) BgColor else Color.White,
            focusedContentColor = BgColor
        ),
        border = ClickableSurfaceDefaults.border(
            focusedBorder = Border(border = BorderStroke(2.dp, TennisYellow))
        )
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}