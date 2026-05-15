package com.demo.androidtv.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// 1. Define the UI State
data class AuthState(
    val selectedTab: AuthTabType = AuthTabType.CODE,
    val emailInput: String = "",
    val passwordInput: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

enum class AuthTabType { EMAIL, CODE }

// 2. Create the ViewModel
class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState.asStateFlow()

    fun selectTab(tab: AuthTabType) {
        _uiState.value = _uiState.value.copy(selectedTab = tab, errorMessage = null)
    }

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(emailInput = email)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(passwordInput = password)
    }

    fun performLogin(onSuccess: () -> Unit) {
        // Prevent double clicks
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            // Set loading state to true
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            // Simulate an API network call to Auth0 / Backend
            delay(1500)

            // On success, reset loading and trigger navigation
            _uiState.value = _uiState.value.copy(isLoading = false)
            onSuccess()
        }
    }
}