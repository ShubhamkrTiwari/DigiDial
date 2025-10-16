package com.bitmax.digidial.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bitmax.digidial.R

@Composable
fun MyProfileScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA))
            .verticalScroll(rememberScrollState())
    ) {
        // 🔹 Header with Profile + Edit button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1976D2))
                .padding(vertical = 32.dp, horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.rahul), // Profile photo
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(3.dp, Color.White, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Rahul Kumar",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "+91 98765 43210",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp
                    )
                }

                // Edit Button
                Button(
                    onClick = { navController.navigate("editprofile") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Edit", color = Color(0xFF1976D2))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Profile Info Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                ProfileDetailItem("Full Name", "Rahul Kumar")
                Divider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
                ProfileDetailItem("Email", "rahul.kumar@example.com")
                Divider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
                ProfileDetailItem("Phone Number", "+91 98765 43210")
                Divider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
                ProfileDetailItem("Date of Birth", "12 Aug 1998")
            }
        }


        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Account Settings
        Text(
            text = "Account Settings",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Gray,
            modifier = Modifier.padding(start = 20.dp, bottom = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                ProfileRow(Icons.Default.Lock, "Change Password") {
                    navController.navigate("change_password")
                }
                Divider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
                ProfileRow(Icons.Default.Settings, "Privacy Settings") {
                    navController.navigate("privacy_settings")
                }
                Divider(color = Color.LightGray.copy(alpha = 0.3f), thickness = 1.dp, modifier = Modifier.padding(horizontal = 20.dp))
                ProfileRow(Icons.Default.Info, "Help & Support") {
                    navController.navigate("helpandsupport")
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ProfileDetailItem(label: String, value: String) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 10.dp)) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.Black)
    }
}
@Composable
fun ProfileRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() } // 👈 navigate here
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF1976D2),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Go",
            tint = Color.Gray
        )
    }
}
@Preview(showBackground = true)
@Composable
fun MyProfileScreenPreview() {
    MyProfileScreen(navController = rememberNavController())
}
