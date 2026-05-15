package com.demo.androidtv

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.demo.androidtv.presentation.AuthState
import com.demo.androidtv.presentation.AuthTabType
import com.demo.androidtv.presentation.AuthViewModel
import com.demo.androidtv.ui.theme.BgColor
import com.demo.androidtv.ui.theme.SurfaceColor
import com.demo.androidtv.ui.theme.TennisYellow
import com.demo.androidtv.ui.theme.TextPrimary
import com.demo.androidtv.ui.theme.TextSecondary

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AuthScreen(
    onLoginClick: () -> Unit,
    viewModel: AuthViewModel = viewModel() // Inject ViewModel
) {
    // Observe the ViewModel state
    val state by viewModel.uiState.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor)
    ) {
        // --- LEFT COLUMN (Form) ---
        Column(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxHeight()
                .padding(horizontal = 50.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Cinematic Text-Based Logo for Auth Screen
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "TENNIS",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CHANNEL",
                    color = TennisYellow,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 4.sp
                )
            }
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                "Sign in to watch\nlive sports.",
                color = TextPrimary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 34.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Premium live streams, multiview, and personalized alerts. Start watching in seconds.",
                color = TextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // --- TABS ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceColor, RoundedCornerShape(8.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.07f), RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
                AuthTab(
                    title = "Email / Password",
                    isSelected = state.selectedTab == AuthTabType.EMAIL,
                    onClick = { viewModel.selectTab(AuthTabType.EMAIL) }
                )
                AuthTab(
                    title = "Sign-In Code",
                    isSelected = state.selectedTab == AuthTabType.CODE,
                    onClick = { viewModel.selectTab(AuthTabType.CODE) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- TAB CONTENT (Passing state down to stateless composables) ---
            if (state.selectedTab == AuthTabType.CODE) {
                CodeLoginView(
                    isLoading = state.isLoading,
                    onLoginClick = { viewModel.performLogin(onLoginClick) }
                )
            } else {
                EmailLoginView(
                    state = state,
                    onEmailChange = { viewModel.updateEmail(it) },
                    onPasswordChange = { viewModel.updatePassword(it) },
                    onLoginClick = { viewModel.performLogin(onLoginClick) }
                )
            }
        }

        // Vertical Divider
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .background(Color.White.copy(alpha = 0.07f))
        )

        // --- RIGHT COLUMN (QR & Pills) ---
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // QR Card
            Box(
                modifier = Modifier
                    .background(SurfaceColor, RoundedCornerShape(16.dp))
                    .border(1.dp, Color.White.copy(alpha = 0.07f), RoundedCornerShape(16.dp))
                    .padding(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Scan to Sign In",
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        MockQRCode()
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Open your phone camera and scan\nto activate on this TV instantly",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "TENNIS CHANNEL.tv/activate/QR8X2",
                        color = TennisYellow,
                        fontSize = 10.sp,
                        modifier = Modifier
                            .background(TennisYellow.copy(alpha = 0.08f), RoundedCornerShape(4.dp))
                            .border(1.dp, TennisYellow.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Content Access Tiers", color = TextSecondary, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AccessPill("⭐ PREMIUM", Color(0xFFFFD600))
                AccessPill("📡 CABLE", Color(0xFFCE93D8))
                AccessPill("✉️ FREE ACCOUNT", Color(0xFF00E676))
            }
            Spacer(modifier = Modifier.height(8.dp))
            AccessPill("🌐 OPEN", TextSecondary)

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .background(Color(0xFF9C27B0).copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                    .border(1.dp, Color(0xFF9C27B0).copy(alpha = 0.4f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 9.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "🔒 WIDEVINE L1 DRM READY",
                    color = Color(0xFFCE93D8),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

// ---------------- Helper Components ----------------

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun RowScope.AuthTab(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .weight(1f)
            .height(40.dp),
        shape = ClickableSurfaceDefaults.shape(shape = RoundedCornerShape(6.dp)),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = if (isSelected) TennisYellow else Color.Transparent,
            focusedContainerColor = Color.White,
            contentColor = if (isSelected) BgColor else TextSecondary,
            focusedContentColor = BgColor
        )
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CodeLoginView(isLoading: Boolean, onLoginClick: () -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceColor, RoundedCornerShape(8.dp))
                .border(1.dp, Color.White.copy(alpha = 0.07f), RoundedCornerShape(8.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "GX9-4RT",
                    color = TennisYellow,
                    fontSize = 36.sp,
                    letterSpacing = 8.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text("Go to ", color = TextSecondary, fontSize = 12.sp)
                    Text("TENNIS CHANNEL.tv/activate", color = TennisYellow, fontSize = 12.sp)
                    Text(
                        " on any device and enter this code",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = ClickableSurfaceDefaults.shape(shape = RoundedCornerShape(8.dp)),
            colors = ClickableSurfaceDefaults.colors(
                containerColor = TennisYellow,
                focusedContainerColor = Color.White,
                contentColor = BgColor,
                focusedContentColor = BgColor
            )
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = BgColor,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text("✓ I've Entered the Code", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun EmailLoginView(
    state: AuthState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Column {
            Text(
                "EMAIL ADDRESS",
                color = TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            TvTextInput(
                value = state.emailInput,
                onValueChange = onEmailChange,
                keyboardType = KeyboardType.Email
            )
        }

        Column {
            Text(
                "PASSWORD",
                color = TextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            TvTextInput(
                value = state.passwordInput,
                onValueChange = onPasswordChange,
                keyboardType = KeyboardType.Password,
                isPassword = true
            )
        }

        Surface(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = ClickableSurfaceDefaults.shape(shape = RoundedCornerShape(8.dp)),
            colors = ClickableSurfaceDefaults.colors(
                containerColor = TennisYellow,
                focusedContainerColor = Color.White,
                contentColor = BgColor,
                focusedContentColor = BgColor
            )
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = BgColor,
                        strokeWidth = 3.dp
                    )
                } else {
                    Text("Sign In with Auth0", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.07f))
            )
            Text(
                "OR",
                color = TextSecondary,
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color.White.copy(alpha = 0.07f))
            )
        }

        Surface(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = ClickableSurfaceDefaults.shape(shape = RoundedCornerShape(8.dp)),
            colors = ClickableSurfaceDefaults.colors(
                containerColor = Color.Transparent,
                focusedContainerColor = Color.White.copy(alpha = 0.2f)
            )
        ) {
            val purpleGradient = Brush.linearGradient(listOf(Color(0xFF6A1B9A), Color(0xFF9C27B0)))
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(purpleGradient),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("📡", fontSize = 14.sp)
                    Text(
                        "TV Provider Sign-In (Adobe Primetime)",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TvTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType,
    isPassword: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = androidx.compose.ui.text.TextStyle(color = TextPrimary, fontSize = 14.sp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        cursorBrush = SolidColor(TennisYellow),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused }
            .background(SurfaceColor, RoundedCornerShape(6.dp))
            .border(
                1.dp,
                if (isFocused) TennisYellow else Color.White.copy(0.07f),
                RoundedCornerShape(6.dp)
            )
            .padding(12.dp)
    )
}

@Composable
fun MockQRCode() {
    val pattern = listOf(
        listOf(1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1),
        listOf(1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0),
        listOf(1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1),
        listOf(1, 0, 1, 1, 1, 0, 1, 0, 0, 1, 1),
        listOf(1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0),
        listOf(1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1),
        listOf(1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1),
        listOf(0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0),
        listOf(1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 1),
        listOf(0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 1),
        listOf(1, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0)
    )

    Canvas(modifier = Modifier.size(100.dp)) {
        val cellSize = size.width / 11f
        for (row in 0 until 11) {
            for (col in 0 until 11) {
                if (pattern[row][col] == 1) {
                    drawRect(
                        color = Color.Black,
                        topLeft = Offset(col * cellSize, row * cellSize),
                        size = Size(cellSize, cellSize)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun AccessPill(text: String, color: Color) {
    Text(
        text = text, color = color, fontSize = 10.sp, fontWeight = FontWeight.Bold,
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(horizontal = 9.dp, vertical = 3.dp)
    )
}