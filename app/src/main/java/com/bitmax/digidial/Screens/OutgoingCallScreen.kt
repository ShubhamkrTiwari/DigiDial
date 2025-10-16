package com.bitmax.digidial.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bitmax.digidial.Models.Customer
import com.bitmax.digidial.R

// ✅ Control item data
data class ControlItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    val iconPainter: Painter? = null,
    val label: String
)

// ✅ Reusable Icon button
@Composable
fun IconActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    iconPainter: Painter? = null,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentSize()
            .clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFF2F5FA),
            tonalElevation = 2.dp,
            modifier = Modifier.size(56.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                when {
                    icon != null -> Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = Color(0xFF1F2937),
                        modifier = Modifier.size(24.dp)
                    )
                    iconPainter != null -> Icon(
                        painter = iconPainter,
                        contentDescription = label,
                        tint = Color(0xFF1F2937),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                color = Color(0xFF374151)
            ),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OutgoingCallScreen(customer: Customer) {
    var callNotes by remember { mutableStateOf("") }

    // 🔹 Controls
    val controls = listOf(
        ControlItem(icon = Icons.Default.Mic, label = "Mute"),
        ControlItem(icon = Icons.AutoMirrored.Filled.VolumeUp, label = "Speaker"),
        ControlItem(icon = Icons.Default.Pause, label = "Hold"),
        ControlItem(icon = Icons.Default.PersonAdd, label = "Add")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 96.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 🔹 Profile + Name + Company + Timer
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    tonalElevation = 4.dp,
                    modifier = Modifier.size(100.dp)
                ) {
                    val imageRes = customer.profileImage ?: R.drawable.ic_avtar
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Avatar",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = customer.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = customer.company ?: "No Company",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White,
                    tonalElevation = 2.dp
                ) {
                    Text(
                        text = customer.currentCallDuration ?: "00:00:00",
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF111111)
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))

            // 🔹 Controls Grid
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
                shape = RoundedCornerShape(14.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth(),
                        userScrollEnabled = false
                    ) {
                        items(controls) { item ->
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .wrapContentSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                IconActionButton(
                                    icon = item.icon,
                                    iconPainter = item.iconPainter,
                                    label = item.label,
                                    onClick = { /* TODO: Add functionality */ }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Small helper dots
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(3) { i ->
                            Box(
                                modifier = Modifier
                                    .size(if (i == 1) 8.dp else 6.dp)
                                    .padding(3.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (i == 1) Color(0xFF3B82F6) else Color(0xFFDDE7FF))
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Notes Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Call Notes/Remarks",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = callNotes,
                        onValueChange = { callNotes = it },
                        placeholder = { Text("Type notes about the call...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp),
                        singleLine = false,
                        maxLines = 3,
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }
        }

        // 🔹 End Call Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = { /* End Call */ },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53935),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CallEnd,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "End Call",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Preview
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewOutgoingCallScreen() {
    OutgoingCallScreen(
        customer = Customer(
            id = 1,
            name = "Olivia Chen",
            company = "Tech Solutions Inc.",
            profileImage = R.drawable.ic_avtar,
            currentCallDuration = "00:05:43",
            phone = "123-456-7890",
            lastCall = "2024-07-30"
        )
    )
}
