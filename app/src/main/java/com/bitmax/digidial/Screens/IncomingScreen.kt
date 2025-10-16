package com.bitmax.digidial.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitmax.digidial.R

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewIncomingScreen() {
    IncomingScreen(
        onAccept = {},
        onDecline = {}
    )
}

@Composable
fun IncomingScreen(
    onAccept: () -> Unit,
    onDecline: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF141E30), Color(0xFF243B55))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- Caller Info Section ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_avtar),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color(0xFF2B9EFF), CircleShape)
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Olivia Chen",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.White
                )
                Text(
                    text = "Tech Solutions Inc.",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(Modifier.height(12.dp))
                Text(
                    text = "(555) 127-6643",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 22.sp,
                    color = Color.White
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Incoming Call...",
                    fontSize = 15.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }

            // --- Action Buttons ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircleActionButton(
                    icon = Icons.Default.CallEnd,
                    text = "Decline",
                    bgColor = Color(0xFFFF5A68),
                    onClick = onDecline
                )
                CircleActionButton(
                    icon = Icons.Default.Call,
                    text = "Accept",
                    bgColor = Color(0xFF35D07F),
                    onClick = onAccept
                )
            }
        }
    }
}

@Composable
fun CircleActionButton(icon: ImageVector, text: String, bgColor: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
