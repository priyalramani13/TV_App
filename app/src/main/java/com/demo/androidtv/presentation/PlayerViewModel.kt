package com.demo.androidtv.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. The state exactly matches what the UI needs to draw
data class PlayerUiState(
    val isLoading: Boolean = true,
    val showError: Boolean = false,
    val showEndScreen: Boolean = false,
    val isPlaying: Boolean = true,
    val streamUrl: String = ""
)

// 2. The ViewModel handles the logic
class PlayerViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState: StateFlow<PlayerUiState> = _uiState.asStateFlow()

    fun loadStream(shouldFail: Boolean, streamType: String) {
        // 1. Determine the URL
        val url = if (streamType == "vod") {
            "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"
        } else {
            "https://feeds.intoday.in/aajtak/api/aajtakhd/master.m3u8"
        }

        _uiState.value = _uiState.value.copy(streamUrl = url)

        // 2. Trigger the correct flow
        if (shouldFail) {
            triggerErrorSequence()
        } else {
            _uiState.value = _uiState.value.copy(isLoading = true, showError = false)
        }
    }

    fun triggerErrorSequence() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, showError = false)
            delay(1500) // Simulate network/DRM load time
            _uiState.value = _uiState.value.copy(isLoading = false, showError = true)
        }
    }

    fun setBuffering(isBuffering: Boolean) {
        if (!_uiState.value.showError) {
            _uiState.value = _uiState.value.copy(isLoading = isBuffering)
        }
    }

    fun triggerEndScreen() {
        _uiState.value = _uiState.value.copy(showEndScreen = true, isPlaying = false)
    }

    fun resetEndScreen() {
        _uiState.value = _uiState.value.copy(showEndScreen = false, isPlaying = true)
    }

    fun togglePlayPause() {
        _uiState.value = _uiState.value.copy(isPlaying = !_uiState.value.isPlaying)
    }
}