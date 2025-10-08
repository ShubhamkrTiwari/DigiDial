package com.bitmax.digidial.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitmax.digidial.network.AuthRepository
import com.bitmax.digidial.network.OtpResponse
import com.bitmax.digidial.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed class OtpUiState {
    object Idle : OtpUiState()
    object Loading : OtpUiState()
    data class Success(val response: OtpResponse) : OtpUiState()
    data class Error(val message: String) : OtpUiState()
}

class AuthViewModel : ViewModel() {

    private val TAG = "AuthViewModel"

    // This correctly initializes the repository, which resolves the error.
    private val repository: AuthRepository = AuthRepository(ApiClient.authApi)

    private val _otpState = MutableStateFlow<OtpUiState>(OtpUiState.Idle)
    val otpState: StateFlow<OtpUiState> = _otpState

    fun sendOtp(mobile: String) {
        viewModelScope.launch {
            _otpState.value = OtpUiState.Loading
            try {
                // This call is now valid because the repository is initialized.
                val response = repository.sendOtp(mobile)
                Log.d(TAG, "OTP API Success: ${response.message}")
                _otpState.value = OtpUiState.Success(response)

            } catch (e: IOException) {
                // The AuthRepository is now designed to throw an IOException on any API or network failure.
                Log.e(TAG, "OTP API Error: ${e.message}", e)
                _otpState.value = OtpUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
