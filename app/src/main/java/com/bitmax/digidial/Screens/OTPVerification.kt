package com.bitmax.digidial.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bitmax.digidial.Navigation.Route
import com.bitmax.digidial.network.ForgetPasswordRequest
import com.bitmax.digidial.network.VerifyOtpRequest
import com.bitmax.digidial.network.ApiClient
import com.bitmax.digidial.network.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun OTPVerificationScreen(navController: NavController, phoneNumber: String) {
    var otp by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isResending by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "OTP Verification",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2196F3)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Enter the OTP sent to $phoneNumber",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { if (it.length <= 6) otp = it.filter { c -> c.isDigit() } },
            placeholder = { Text("Enter 6-digit OTP") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // ✅ VERIFY OTP BUTTON
        Button(
            onClick = {
                coroutineScope.launch {
                    isLoading = true
                    errorMessage = null
                    successMessage = null

                    try {
                        val response = ApiClient.authApi.verifyOtp(
                            VerifyOtpRequest(mobile = "+91 $phoneNumber", otp = otp)
                        )

                        if (response.isSuccessful && response.body()?.success == true) {
                            successMessage = "✅ OTP Verified! Redirecting..."
                            // Delay a bit for a smooth UX
                            //delay(1200)
                            // ✅ Go to Dashboard and clear backstack (so user can't go back)
                            withContext(Dispatchers.Main) {
                                navController.navigate("dashboard") {
                                    popUpTo("otpverfication/{phoneNumber}") { inclusive = true }
                                }
                            }

                        } else {
                            errorMessage = response.body()?.message ?: "Invalid OTP. Please try again."
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "An unexpected error occurred."
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = otp.length == 6 && !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
            } else {
                Text("Verify OTP", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
        }

        successMessage?.let {
            Text(text = it, color = Color(0xFF008000), fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ✅ RESEND OTP
        Text(
            text = if (isResending) "Sending..." else "Resend OTP",
            color = if (isResending) Color.Gray else Color(0xFF2196F3),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable(enabled = !isResending) {
                coroutineScope.launch {
                    isResending = true
                    errorMessage = null
                    successMessage = null
                    try {
                        val response = ApiClient.authApi.sendOtp(
                            ForgetPasswordRequest(mobile = "+91 $phoneNumber")
                        )
                        if (response.isSuccessful && response.body()?.success == true) {
                            successMessage = response.body()?.message ?: "OTP sent successfully!"
                            otp = ""
                        } else {
                            errorMessage = response.body()?.message ?: "Failed to send OTP."
                        }
                    } catch (e: Exception) {
                        errorMessage = e.message ?: "An unexpected error occurred."
                    } finally {
                        isResending = false
                    }
                }
            }
        )
    }
}