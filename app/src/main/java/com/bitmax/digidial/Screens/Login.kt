package com.bitmax.digidial.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
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
import com.bitmax.digidial.R
import com.bitmax.digidial.viewmodel.AuthViewModel
import com.bitmax.digidial.viewmodel.OtpUiState
import kotlinx.coroutines.delay


@Composable
fun Login(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var mobileNumber by rememberSaveable { mutableStateOf("") }
    val otpState by viewModel.otpState.collectAsState()

    LaunchedEffect(otpState) {
        if (otpState is OtpUiState.Success) {
            delay(1500) // 1.5 sec delay for showing "Redirecting..."
            val cleanNumber = mobileNumber.trim()
            navController.navigate("otpverification/$cleanNumber"){
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFF3E7E9), Color(0xFFE3EEFF))
                )
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Image(
            painter = painterResource(id = R.drawable.digidial_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .height(130.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Log in to your account",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D2D44),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter your mobile number to continue",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        OutlinedTextField(
            value = mobileNumber,
            onValueChange = { input ->
                if (input.length <= 10) mobileNumber = input.filter { it.isDigit() }
            },
            placeholder = { Text("Enter Mobile Number") },
            label = { Text("Mobile Number") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            leadingIcon = { Text(text = "+91 ", fontWeight = FontWeight.Bold, color = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color(0xFF2B9EFF),
                unfocusedIndicatorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val fullMobileNumber = "+91 $mobileNumber"
                Log.d("LoginScreen", "Sending OTP to: $fullMobileNumber")
                viewModel.sendOtp(fullMobileNumber)
            },
            enabled = mobileNumber.length == 10,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(
                    elevation = if (mobileNumber.length == 10) 12.dp else 0.dp,
                    shape = RoundedCornerShape(12.dp)
                ),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (mobileNumber.length == 10) Color(0xFF2196F3) else Color.Gray,
                contentColor = Color.White
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send OTP"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Send OTP",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (otpState) {
            is OtpUiState.Loading -> {
                CircularProgressIndicator()
            }

            is OtpUiState.Success -> {
                val response = (otpState as OtpUiState.Success).response
                Text(
                    text = "✅ ${response.message}\nRedirecting to verification...",
                    color = Color(0xFF00C853),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }

            is OtpUiState.Error -> {
                val msg = (otpState as OtpUiState.Error).message
                Text(
                    text = "❌ $msg",
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            else -> {}
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Powered by Bitmax🩵",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    Login(navController = rememberNavController())
}
