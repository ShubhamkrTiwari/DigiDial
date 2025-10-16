package com.bitmax.digidial.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.bitmax.digidial.navigation.Route
import com.bitmax.digidial.viewmodel.AuthViewModel
import com.bitmax.digidial.viewmodel.VerifyOtpUiState

@Composable
fun OTPVerificationScreen(navController: NavController, phoneNumber: String, authViewModel: AuthViewModel = viewModel()) {
    val context = LocalContext.current
    var otpValue by remember { mutableStateOf("") }
    val verifyOtpState by authViewModel.verifyOtpState.collectAsState()
    val fullPhoneNumber = "+91$phoneNumber"

    // Navigate to home screen on successful OTP verification
    LaunchedEffect(verifyOtpState) {
        if (verifyOtpState is VerifyOtpUiState.Success) {
            navController.navigate(Route.HomeScreen.route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3EEFF), Color(0xFFF3E7E9))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Verify OTP",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D2D44)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Enter the OTP sent to $fullPhoneNumber",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = otpValue,
            onValueChange = { if (it.length <= 6) otpValue = it },
            label = { Text("Enter 6-Digit OTP") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (otpValue.length == 6) {
                    authViewModel.verifyOtp(fullPhoneNumber, otpValue, context)
                }
            },
            enabled = otpValue.length == 6,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Verify", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // UI to show the current state
        Box(modifier = Modifier.height(60.dp), contentAlignment = Alignment.Center) {
            when (val state = verifyOtpState) {
                is VerifyOtpUiState.Loading -> {
                    CircularProgressIndicator()
                }
                is VerifyOtpUiState.Error -> {
                    Text(
                        text = "Verification Failed: ${state.message}",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
                is VerifyOtpUiState.Success -> {
                    Text(
                        text = "Verification Successful! Redirecting...",
                        color = Color(0xFF00C853),
                        textAlign = TextAlign.Center
                    )
                }
                else -> {}
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OTPVerificationScreenPreview() {
    OTPVerificationScreen(rememberNavController(), "1234567890")
}
