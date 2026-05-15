package com.demo.androidtv

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Text
import com.demo.androidtv.ui.theme.BgColor
import com.demo.androidtv.ui.theme.Surface2Color
import com.demo.androidtv.ui.theme.TennisYellow
import com.demo.androidtv.ui.theme.TextSecondary

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SplashScreen(onNavigate: () -> Unit) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2800)
        onNavigate()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            // Bold, purely typographic logo
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "TENNIS",
                    color = Color.White,
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "CHANNEL",
                    color = TennisYellow,
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 8.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "ANDROID TV · GOOGLE TV",
                color = TextSecondary,
                fontSize = 14.sp,
                letterSpacing = 8.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Sleek Loading Bar
            Box(modifier = Modifier
                .width(240.dp)
                .height(2.dp)
                .background(Surface2Color)) {
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
                    .background(TennisYellow))
            }
        }
    }
}