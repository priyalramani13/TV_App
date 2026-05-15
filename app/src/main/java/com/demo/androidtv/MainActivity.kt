package com.demo.androidtv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Surface
import com.demo.androidtv.ui.theme.BgColor

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalTvMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BgColor)
            ) {
                val navController = rememberNavController()

                // The Routing Map
                NavHost(navController = navController, startDestination = "splash") {

                    composable("splash") {
                        SplashScreen(
                            onNavigate = {
                                // Pop splash off the backstack so user can't hit "back" to return to it
                                navController.navigate("auth") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("auth") {
                        AuthScreen(
                            onLoginClick = {
                                navController.navigate("home") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("home") {
                        HomeScreen(
                            // Now passing BOTH the error flag and the stream type from the MVVM Match data
                            onMatchClick = { shouldFail, streamType ->
                                navController.navigate("player/$shouldFail/$streamType")
                            }
                        )
                    }

                    // Added the {streamType} argument to the route
                    composable("player/{shouldFail}/{streamType}") { backStackEntry ->
                        val shouldFail =
                            backStackEntry.arguments?.getString("shouldFail")?.toBoolean() ?: false
                        val streamType = backStackEntry.arguments?.getString("streamType") ?: "live"

                        PlayerScreen(
                            shouldFail = shouldFail,
                            streamType = streamType, // Pass it down to the PlayerScreen
                            onBackPress = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}