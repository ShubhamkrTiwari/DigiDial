package com.bitmax.digidial.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bitmax.digidial.R

@Preview(showBackground = true)
@Composable
 fun AgentDashboardScreenPreview() {
    AgentDashboardScreen(navController = NavController(LocalContext.current))
}

@Composable
fun AgentDashboardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F2F5))
    ) {
        // 🔷 Profile Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0288D1)) // Light blue
                .padding(vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(contentAlignment = Alignment.BottomEnd) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_agent), // ✅ replace with your drawable
                        contentDescription = "Agent Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(3.dp, Color.White, CircleShape)
                    )

                    IconButton(
                        onClick = { /* TODO: Handle edit profile */ },
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFF0288D1), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Agent Profile",
                            tint = Color(0xFF0288D1)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Amit Sharma",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Agent ID: AG-2048",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Dashboard Options
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                AgentDashboardRow(Icons.Default.Work, "Assigned Leads") {
                    navController.navigate("assignedleads")
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                AgentDashboardRow(Icons.Default.Call, "Recent Calls") {
                    navController.navigate("recentcalls")
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                AgentDashboardRow(Icons.Default.Notifications, "Notifications") {
                    navController.navigate("notification")
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                AgentDashboardRow(Icons.Default.Settings, "Settings") {
                    navController.navigate("settings")
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                AgentDashboardRow(Icons.Default.Info, "About Company") {
                    navController.navigate("aboutus")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔴 Logout Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
            ) {
                AgentDashboardRow(Icons.Default.Close, "Logout", isLogout = true) {
                    navController.navigate("login") {
                        popUpTo("agentdashboard") { inclusive = true }
                    }
                }
            }
        }
    }
}

@Composable
fun AgentDashboardRow(
    icon: ImageVector,
    title: String,
    isLogout: Boolean = false,
    onClick: () -> Unit
) {
    val contentColor = if (isLogout) Color.Red else Color(0xFF0288D1)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(contentColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            color = if (isLogout) contentColor else Color.DarkGray,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        if (!isLogout) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Navigate",
                tint = Color.Gray
            )
        }
    }
}
