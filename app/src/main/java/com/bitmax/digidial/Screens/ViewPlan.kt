package com.bitmax.digidial.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


class ViewPlan : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DigiDialTheme {
                ViewPlanScreen(navController = rememberNavController())
            }
        }
    }
}

data class Plan(val name: String, val priceMonthly: Int, val priceYearly: Int, val features: List<String>, val isPopular: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewPlanScreen(navController: NavController) {
    val plans = listOf(
        Plan("Basic", 499, 4999, listOf("1 Business Number", "1 Team Member", "1000 Calling Minutes"), isPopular = false),
        Plan("Premium", 999, 9999, listOf("2 Business Numbers", "5 Team Members", "Unlimited Calling Minutes", "Call Recording"), isPopular = true),
        Plan("Business", 1999, 19999, listOf("5 Business Numbers", "Unlimited Team Members", "Unlimited Calling Minutes", "Advanced Analytics"), isPopular = false)
    )

    var isYearly by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val skyBlue = Color(0xFF03A9F4) // Light Sky Blue

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose Your Plan", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFE1F5FE), Color.White)
                    )
                )
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Monthly/Yearly Toggle
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color.LightGray.copy(alpha = 0.3f))
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { isYearly = false },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isYearly) skyBlue else Color.Transparent,
                        contentColor = if (!isYearly) Color.White else Color.Black
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)

                ) {
                    Text("Monthly")
                }
                Button(
                    onClick = { isYearly = true },
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isYearly) skyBlue else Color.Transparent,
                        contentColor = if (isYearly) Color.White else Color.Black
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text("Yearly")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pricing Cards
            plans.forEach { plan ->
                PricingCard(plan = plan, isYearly = isYearly, highlightColor = skyBlue)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun PricingCard(plan: Plan, isYearly: Boolean, highlightColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .border(
                width = if (plan.isPopular) 2.dp else 1.dp,
                color = if (plan.isPopular) highlightColor else highlightColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            if (plan.isPopular) {
                Text(
                    text = "POPULAR",
                    color = highlightColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(highlightColor.copy(alpha = 0.1f))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(plan.name, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "₹${if (isYearly) plan.priceYearly else plan.priceMonthly}",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = highlightColor
                )
                Text(
                    text = if (isYearly) "/year" else "/month",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            plan.features.forEach { feature ->
                FeatureItem(text = feature, highlightColor = highlightColor)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Handle plan selection */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if(plan.isPopular) highlightColor else highlightColor.copy(alpha = 0.1f),
                    contentColor = if(plan.isPopular) Color.White else highlightColor
                )
            ) {
                Text("Choose Plan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FeatureItem(text: String, highlightColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Included",
            tint = highlightColor,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, fontSize = 15.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ViewPlanScreenPreview() {
    DigiDialTheme {
        ViewPlanScreen(navController = rememberNavController())
    }
}

@Composable
fun DigiDialTheme(content: @Composable () -> Unit) {
    TODO("Not yet implemented")
}
