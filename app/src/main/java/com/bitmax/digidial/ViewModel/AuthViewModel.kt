package com.bitmax.digidial.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitmax.digidial.network.AuthRepository
import com.bitmax.digidial.network.ApiClient
import com.bitmax.digidial.network.OtpResponse
import com.bitmax.digidial.network.VerifyOtpResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

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
            try {
                val response = repository.sendOtp(mobile)
                Log.d(TAG, "OTP API Success: ${response.message}")
                _otpState.value = OtpUiState.Success(response)

            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: "An unexpected error occurred"
                Log.e(TAG, "OTP API Error: $errorMessage", e)
                _otpState.value = OtpUiState.Error(errorMessage)
            } catch (e: IOException) {
                Log.e(TAG, "OTP API Error: ${e.message}", e)
                _otpState.value = OtpUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun verifyOtp(mobile: String, otp: String, context: Context) {
        viewModelScope.launch {
            _verifyOtpState.value = VerifyOtpUiState.Loading
            try {
                val response = repository.verifyOtp(mobile, otp)
                Log.d(TAG, "Verify OTP Success: ${response.message}")

                // Save the token
                val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("auth_token", response.token)
                    apply()
                }

                // Save the phone number
                _phoneNumber.value = mobile

                _verifyOtpState.value = VerifyOtpUiState.Success(response)
            } catch (e: HttpException) {
                val errorMessage = e.response()?.errorBody()?.string() ?: "An unexpected error occurred"
                Log.e(TAG, "Verify OTP Error: $errorMessage", e)
                _verifyOtpState.value = VerifyOtpUiState.Error(errorMessage)
            } catch (e: IOException) {
                Log.e(TAG, "Verify OTP Error: ${e.message}", e)
                _verifyOtpState.value = VerifyOtpUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
