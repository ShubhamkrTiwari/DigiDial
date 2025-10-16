package com.bitmax.digidial.Screens

import android.content.Context
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
import com.bitmax.digidial.viewmodel.OtpUiState
import com.bitmax.digidial.viewmodel.VerifyOtpUiState
import kotlinx.coroutines.delay

@Composable
fun OTPVerificationScreen(navController: NavController, phoneNumber: String, authViewModel: AuthViewModel = viewModel()) {
    var otp by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    val verifyOtpState by authViewModel.verifyOtpState.collectAsState()
    val resendOtpState by authViewModel.otpState.collectAsState()

    LaunchedEffect(verifyOtpState) {
        if (verifyOtpState is VerifyOtpUiState.Success) {
            val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putBoolean("isLoggedIn", true)
                apply()
            }
            delay(1500)
            navController.navigate(Route.HomeScreen.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFF0F4F8), Color(0xFFE3EEFF))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Mail,
                    contentDescription = "OTP Icon",
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "OTP Verification",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Enter the OTP sent to +91 $phoneNumber",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(30.dp))

                OtpTextField(
                    otpText = otp,
                    onOtpTextChange = { value, _ ->
                        if (value.length <= 6) {
                            otp = value.filter { it.isDigit() }
                        }
                    },
                    otpCount = 6
                )

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = { authViewModel.verifyOtp("+91 $phoneNumber", otp, context) },
                    enabled = otp.length == 6 && verifyOtpState !is VerifyOtpUiState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (verifyOtpState is VerifyOtpUiState.Loading) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("VERIFY OTP", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.height(20.dp), contentAlignment = Alignment.Center) {
                    when (val state = verifyOtpState) {
                        is VerifyOtpUiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error, fontSize = 14.sp, textAlign = TextAlign.Center)
                        is VerifyOtpUiState.Success -> Text(text = "✅ Verification Successful! Redirecting...", color = Color(0xFF2E7D32), fontSize = 14.sp, textAlign = TextAlign.Center)
                        else -> {}
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Didn't receive the OTP? ", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    val isResending = resendOtpState is OtpUiState.Loading
                    Text(
                        text = if (isResending) "Sending..." else "Resend OTP",
                        color = if (isResending) Color.Gray else MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .clickable(enabled = !isResending) { authViewModel.sendOtp("+91$phoneNumber") }
                            .padding(4.dp)
                    )
                }
                
                when (val state = resendOtpState) {
                    is OtpUiState.Success -> Text(text = state.response.message, color = Color(0xFF2E7D32), fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                    is OtpUiState.Error -> Text(text = state.message, color = MaterialTheme.colorScheme.error, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                    else -> {}
                }
            }
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
                        index < otpText.length -> otpText[index].toString()
                        else -> ""
                    }
                    val isFocused = otpText.length == index
                    Box(
                        modifier = Modifier
                            .width(38.dp)
                            .height(38.dp)
                            .border(
                                1.5.dp, when {
                                    isFocused -> MaterialTheme.colorScheme.primary
                                    else -> Color.LightGray
                                }, RoundedCornerShape(8.dp)
                            )
                            .background(Color.Transparent, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = char,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Black,
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OTPVerificationScreenPreview() {
    OTPVerificationScreen(navController = rememberNavController(), phoneNumber = "1234567890")
}
