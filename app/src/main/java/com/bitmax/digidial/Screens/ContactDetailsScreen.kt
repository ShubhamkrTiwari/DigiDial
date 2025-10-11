package com.bitmax.digidial.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import java.net.URLEncoder
import com.bitmax.digidial.Models.Customer

@Composable
fun ContactDetailsScreen(customer: Customer, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFFF8FBFF), Color.White))
            )
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔹 Profile Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp), // top gap
            contentAlignment = Alignment.Center
        ) {
            // Centered Profile Avatar
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = customer.name.first().toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                )
            }

            // Edit button at top-right corner of the Box
            FloatingActionButton(
                onClick = { /* Edit Contact */ },
                containerColor = Color(0xFF2196F3),
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.TopEnd) // top-right corner
            ) {
                Icon(Icons.Default.ModeEdit, contentDescription = "Edit", tint = Color.White)
            }
        }


        Spacer(modifier = Modifier.height(12.dp))

        Text(customer.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2D2D2D))
        Text(customer.type, fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(28.dp))

        // 🔹 Contact Info Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                ContactInfoRow(Icons.Default.Call, "Mobile", customer.phone ?: "N/A")
                Spacer(Modifier.height(14.dp))
                ContactInfoRow(Icons.Default.Email, "Email", customer.email ?: "N/A")
                Spacer(Modifier.height(14.dp))
                ContactInfoRow(Icons.Default.Home, "Last Call", customer.lastCall ?: "N/A")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Call & Message Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            val customerJson = URLEncoder.encode(Gson().toJson(customer), "UTF-8")

            Button(
                onClick = {
                    // ✅ Navigate to OutgoingCallScreen passing customer JSON
                    navController.navigate("outgoingCall/$customerJson")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White)
                Spacer(Modifier.width(6.dp))
                Text("Call", color = Color.White, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { /* Message action */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Message, contentDescription = "Message", tint = Color.White)
                Spacer(Modifier.width(6.dp))
                Text("Message", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 🔹 Call History Card
        Text("Call History with ${customer.name}", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (customer.callHistory.isEmpty()) {
                    Text("No call history available", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(10.dp))
                } else {
                    customer.callHistory.forEachIndexed { index, history ->
                        HistoryItem(history)
                        if (index < customer.callHistory.lastIndex) Divider()
                    }
                }
            }
        }
    }
}

@Composable
fun ContactInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = label, tint = Color(0xFF2196F3))
        Spacer(Modifier.width(10.dp))
        Column {
            Text(label, fontWeight = FontWeight.Medium, fontSize = 13.sp, color = Color.Gray)
            Text(value, fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun HistoryItem(text: String) {
    Text(text, fontSize = 14.sp, color = Color(0xFF2D2D2D), modifier = Modifier.padding(vertical = 10.dp))
}

