package com.bitmax.digidial.Screens

import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.*
import com.bitmax.digidial.R
import com.bitmax.digidial.Navigation.Route
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    // ⏳ Navigate to SwitchAccountScreen after 3 seconds
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("switch_account")
    }

    // 🎨 UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2196F3))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 🟦 Logo
        Image(
            painter = painterResource(id = R.drawable.digidial_logo),
            contentDescription = "DigiDial Logo",
            modifier = Modifier
                .height(300.dp)
                .width(200.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 🌀 Lottie Animation
        LottieAnimationView(animationRes = R.raw.superfone_lottie)

        Spacer(modifier = Modifier.height(12.dp))

        // ✨ Tagline
        Text(
            text = "Your Business, Your Phone",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Manage calls, CRM, and teams with ease",
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun LottieAnimationView(
    @RawRes animationRes: Int,
    modifier: Modifier = Modifier,
    iterations: Int = LottieConstants.IterateForever
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
            .size(190.dp)
            .clip(RoundedCornerShape(15.dp))
    )
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}
