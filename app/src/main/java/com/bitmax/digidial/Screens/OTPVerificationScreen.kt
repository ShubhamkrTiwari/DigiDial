package com.bitmax.digidial.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bitmax.digidial.viewmodel.AuthViewModel
import com.bitmax.digidial.viewmodel.VerifyOtpUiState

@Composable
fun OTPVerificationScreen(navController: NavController, phoneNumber: String, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    val verifyOtpState by authViewModel.verifyOtpState.collectAsState()
    val fullPhoneNumber = "+91$phoneNumber"

    // Automatically trigger OTP verification
    LaunchedEffect(Unit) {
        authViewModel.verifyOtp(fullPhoneNumber, "1234", context) // Using a dummy OTP
    }

    // Handle navigation as a side effect
    LaunchedEffect(verifyOtpState) {
        if (verifyOtpState is VerifyOtpUiState.Success) {
            navController.navigate("homeScreen/$fullPhoneNumber") {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }
    }

    // UI to show the current state
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = verifyOtpState) {
            is VerifyOtpUiState.Loading -> {
                CircularProgressIndicator()
            }
            is VerifyOtpUiState.Error -> {
                Text(text = "Verification Failed: ${state.message}")
            }
            is VerifyOtpUiState.Idle -> {
                Text(text = "Verifying OTP...")
            }
            is VerifyOtpUiState.Success -> {
                Text(text = "Verification Successful! Redirecting...")
            }
        }
    }
}
