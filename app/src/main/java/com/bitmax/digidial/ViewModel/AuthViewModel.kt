package com.bitmax.digidial.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitmax.digidial.network.AuthRepository
import com.bitmax.digidial.network.ApiClient
import com.bitmax.digidial.network.NetworkResult
import com.bitmax.digidial.network.OtpResponse
import com.bitmax.digidial.network.VerifyOtpResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class OtpUiState {
    object Idle : OtpUiState()
    object Loading : OtpUiState()
    data class Success(val response: OtpResponse) : OtpUiState()
    data class Error(val message: String) : OtpUiState()
}

sealed class VerifyOtpUiState {
    object Idle : VerifyOtpUiState()
    object Loading : VerifyOtpUiState()
    data class Success(val response: VerifyOtpResponse) : VerifyOtpUiState()
    data class Error(val message: String) : VerifyOtpUiState()
}

class AuthViewModel : ViewModel() {

    private val TAG = "AuthViewModel"

    private val repository: AuthRepository = AuthRepository(ApiClient.authApi)

    private val _otpState = MutableStateFlow<OtpUiState>(OtpUiState.Idle)
    val otpState: StateFlow<OtpUiState> = _otpState

    private val _verifyOtpState = MutableStateFlow<VerifyOtpUiState>(VerifyOtpUiState.Idle)
    val verifyOtpState: StateFlow<VerifyOtpUiState> = _verifyOtpState

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber.asStateFlow()

    fun sendOtp(mobile: String) {
        viewModelScope.launch {
            _otpState.value = OtpUiState.Loading
            when (val response = repository.sendOtp(mobile)) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "OTP API Success: ${response.data.message}")
                    _otpState.value = OtpUiState.Success(response.data)
                }
                is NetworkResult.Error -> {
                    Log.e(TAG, "OTP API Error: ${response.message}")
                    _otpState.value = OtpUiState.Error(response.message ?: "An unknown error occurred")
                }
                is NetworkResult.Exception -> {
                    Log.e(TAG, "OTP API Exception: ${response.e.message}", response.e)
                    _otpState.value = OtpUiState.Error(response.e.message ?: "An unexpected error occurred")
                }
            }
        }
    }

    fun verifyOtp(mobile: String, otp: String, context: Context) {
        viewModelScope.launch {
            _verifyOtpState.value = VerifyOtpUiState.Loading
            when (val response = repository.verifyOtp(mobile, otp)) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "Verify OTP Success: ${response.data.message}")

                    // Save the token
                    val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("auth_token", response.data.token)
                        apply()
                    }

                    // Save the phone number
                    _phoneNumber.value = mobile

                    _verifyOtpState.value = VerifyOtpUiState.Success(response.data)
                }
                is NetworkResult.Error -> {
                    Log.e(TAG, "Verify OTP Error: ${response.message}")
                    _verifyOtpState.value = VerifyOtpUiState.Error(response.message ?: "An unknown error occurred")
                }
                is NetworkResult.Exception -> {
                    Log.e(TAG, "Verify OTP Exception: ${response.e.message}", response.e)
                    _verifyOtpState.value = VerifyOtpUiState.Error(response.e.message ?: "An unexpected error occurred")
                }
            }
        }
    }
}
