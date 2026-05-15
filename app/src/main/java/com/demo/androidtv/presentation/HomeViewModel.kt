package com.demo.androidtv.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.demo.androidtv.domain.Match
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {

    private val _liveMatches = MutableStateFlow<List<Match>>(emptyList())
    val liveMatches: StateFlow<List<Match>> = _liveMatches.asStateFlow()

    private val _upcomingMatches = MutableStateFlow<List<Match>>(emptyList())
    val upcomingMatches: StateFlow<List<Match>> = _upcomingMatches.asStateFlow()

    init {
        loadMockData()
    }

    private fun loadMockData() {
        // --- 1. LIVE MATCHES ---
        _liveMatches.value = listOf(
            Match(
                id = "1",
                sport = "TENNIS",
                title = "Wimbledon QF - Court 1",
                meta = "Murray vs Medvedev",
                score = "6-4 3-2",
                emoji = "🎾",
                isLive = true,
                tag = "PRE",
                tagColor = Color(0xFFFFD600),
                tagText = Color.Black,
                bgGradient = listOf(Color(0xFF1A3A0A), Color(0xFF2A5A10)),
                streamType = "live",
                shouldFail = false
            ),
            Match(
                id = "2",
                sport = "FOOTBALL",
                title = "Man City vs Arsenal",
                meta = "Premier League · Etihad",
                score = "1-0",
                emoji = "⚽",
                isLive = true,
                tag = "CABLE",
                tagColor = Color(0xFF9C27B0),
                tagText = Color.White,
                bgGradient = listOf(Color(0xFF2A0A3A), Color(0xFF4A1060)),
                streamType = "live",
                shouldFail = true // This triggers our error screen!
            ),
            Match(
                id = "3",
                sport = "BASKETBALL",
                title = "NBA Playoffs - Game 5",
                meta = "Lakers vs Celtics",
                score = "94-88",
                emoji = "🏀",
                isLive = true,
                tag = "PRE",
                tagColor = Color(0xFFFFD600),
                tagText = Color.Black,
                bgGradient = listOf(Color(0xFF0A2A4A), Color(0xFF0D3A6E)),
                streamType = "vod",
                shouldFail = false // This plays Bunny Video!
            ),
            Match(
                id = "4",
                sport = "BASEBALL",
                title = "MLB Regular Season",
                meta = "Yankees vs Red Sox",
                score = "4-2",
                emoji = "⚾",
                isLive = true,
                tag = "FREE",
                tagColor = Color(0xFF00E676),
                tagText = Color.Black,
                bgGradient = listOf(Color(0xFF1B263B), Color(0xFF0D1B2A)),
                streamType = "live",
                shouldFail = false
            ),
            Match(
                id = "5",
                sport = "FORMULA 1",
                title = "Monaco Grand Prix",
                meta = "Live Race",
                score = "Lap 42",
                emoji = "🏎️",
                isLive = true,
                tag = "PRE",
                tagColor = Color(0xFFFFD600),
                tagText = Color.Black,
                bgGradient = listOf(Color(0xFF4A0A0A), Color(0xFF800000)),
                streamType = "live",
                shouldFail = false
            ),
            Match(
                id = "6",
                sport = "GOLF",
                title = "Masters Tournament",
                meta = "Final Round · Augusta",
                score = "-12",
                emoji = "⛳",
                isLive = true,
                tag = "CABLE",
                tagColor = Color(0xFF9C27B0),
                tagText = Color.White,
                bgGradient = listOf(Color(0xFF0A4F2A), Color(0xFF0D6334)),
                streamType = "live",
                shouldFail = false
            )
        )

        // --- 2. COMING UP TODAY ---
        _upcomingMatches.value = listOf(
            Match(
                id = "7",
                sport = "BASEBALL",
                title = "World Series G1",
                meta = "Yankees vs Dodgers",
                emoji = "⚾",
                isLive = false,
                tag = "PRE",
                tagColor = Color(0xFFFFD600),
                tagText = Color.Black,
                bgGradient = listOf(Color(0xFF1A1A2A), Color(0xFF2A2A40)),
                streamType = "live"
            ),
            Match(
                id = "8",
                sport = "SWIMMING",
                title = "Olympic Trials 200M",
                meta = "US Swimming",
                emoji = "🏊",
                isLive = false,
                tag = "FREE",
                tagColor = Color(0xFF00E676),
                tagText = Color.Black,
                bgGradient = listOf(Color(0xFF0A2A4A), Color(0xFF0D3A6E)),
                streamType = "live"
            ),
            Match(
                id = "9",
                sport = "HORSE RACING",
                title = "Breeders Cup Classic",
                meta = "Santa Anita Park",
                emoji = "🏇",
                isLive = false,
                tag = "CABLE",
                tagColor = Color(0xFF9C27B0),
                tagText = Color.White,
                bgGradient = listOf(Color(0xFF1A3A0A), Color(0xFF2A5A10)),
                streamType = "live"
            ),
            Match(
                id = "10",
                sport = "BOXING",
                title = "Heavyweight Title",
                meta = "O2 Arena, London",
                emoji = "🥊",
                isLive = false,
                tag = "PRE",
                tagColor = Color(0xFFFFD600),
                tagText = Color.Black,
                bgGradient = listOf(Color(0xFF4A0A0A), Color(0xFF2A0A0A)),
                streamType = "live"
            ),
            Match(
                id = "11",
                sport = "TENNIS",
                title = "US Open Final",
                meta = "Alcaraz vs Sinner",
                emoji = "🏆",
                isLive = false,
                tag = "PRE",
                tagColor = Color(0xFFFFD600),
                tagText = Color.Black,
                bgGradient = listOf(Color(0xFF1A1A2A), Color(0xFF2A2A40)),
                streamType = "live"
            ),
            Match(
                id = "12",
                sport = "SOCCER",
                title = "Champions League",
                meta = "Real Madrid vs Bayern",
                emoji = "⚽",
                isLive = false,
                tag = "CABLE",
                tagColor = Color(0xFF9C27B0),
                tagText = Color.White,
                bgGradient = listOf(Color(0xFF2A0A3A), Color(0xFF4A1060)),
                streamType = "live"
            ),
            Match(
                id = "13",
                sport = "UFC",
                title = "UFC 305 Main Event",
                meta = "Las Vegas, NV",
                emoji = "🥋",
                isLive = false,
                tag = "PRE",
                tagColor = Color(0xFFFFD600),
                tagText = Color.Black,
                bgGradient = listOf(Color(0xFF1B263B), Color(0xFF0D1B2A)),
                streamType = "live"
            )
        )
    }
}