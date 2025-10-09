package com.bitmax.digidial.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bitmax.digidial.network.ForgetPasswordRequest
import com.bitmax.digidial.network.VerifyOtpRequest
import com.bitmax.digidial.network.ApiClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun OTPVerificationScreen(navController: NavController, phoneNumber: String) {
    var otp by rememberSaveable { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isResending by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF87CEEB), // Sky Blue
                        Color.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = Icons.Outlined.Mail,
                contentDescription = "OTP Icon",
                modifier = Modifier.size(80.dp),
                tint = Color(0xFF2196F3)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "OTP Verification",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Enter the OTP sent to your number",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            OtpTextField(
                otpText = otp,
                onOtpTextChange = { value, _ ->
                    if (value.length <= 6) {
                        otp = value.filter { it.isDigit() }
                    }
                },
                otpCount = 6
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

                            Log.d("OTPVerification", "Response code: ${response.code()}")
                            Log.d("OTPVerification", "Response body: ${response.body()?.toString()}")

                            if (response.isSuccessful && response.body()?.success == true) {
                                Log.d("OTPVerification", "OTP Verified Successfully")
                                successMessage = "✅ Login Successful! Redirecting..."

                                delay(2000)

                                Log.d("OTPVerification", "Navigating to homeScreen now")

                                navController.navigate("homeScreen") {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }
                            } else {
                                Log.d("OTPVerification", "OTP verification failed: ${response.body()?.message}")
                                errorMessage = response.body()?.message ?: "Invalid OTP"
                            }
                        } catch (e: Exception) {
                            Log.e("OTPVerification", "Exception: ${e.message}")
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
                    Text("VERIFY OTP", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error, fontSize = 14.sp)
            }

            successMessage?.let {
                Text(text = it, color = Color(0xFF008000), fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Didn't Receive OTP?", color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            // ✅ RESEND OTP
            Text(
                text = if (isResending) "Sending..." else "RESEND OTP?",
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
}

@Composable
fun OtpTextField(
    modifier: Modifier = Modifier,
    otpText: String,
    otpCount: Int,
    onOtpTextChange: (String, Boolean) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = otpText,
        onValueChange = {
            if (it.length <= otpCount) {
                onOtpTextChange.invoke(it, it.length == otpCount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(otpCount) { index ->
                    val char = when {
                        index >= otpText.length -> "0"
                        else -> otpText[index].toString()
                    }
                    val isFocused = otpText.length == index
                    Box(
                        modifier = Modifier
                            .width(45.dp)
                            .height(45.dp)
                            .border(
                                1.dp, when {
                                    isFocused -> Color(0xFF2196F3)
                                    else -> Color.LightGray
                                }, RoundedCornerShape(8.dp)
                            )
                            .background(Color.White, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (index >= otpText.length) Color.LightGray else Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                    if (index < otpCount - 1) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }
    )
}
