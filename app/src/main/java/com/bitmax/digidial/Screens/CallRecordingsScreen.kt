package com.bitmax.digidial.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Colors
private val SurfaceWhite = Color(0xFFFFFFFF)
private val ItemCardGray = Color(0xFFF7FAFC)
private val TitleText = Color(0xFF0F172A)

data class RecordingItem(
    val id: Int,
    val title: String,
    val subTitle: String,
)


@Composable
fun CallRecordingsScreen(navController: NavController) {
    // Sample data
    val recordingItems = listOf(
        RecordingItem(1, "Call Recordings", "10:21, 1:30 AM"),
        RecordingItem(2, "David Lee", "20:19, 2:00 AM"),
        RecordingItem(3, "New Lead", "20:10, 1:35 AM"),
    )

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = PrimaryBlue,
            onPrimary = Color.White,
            background = Color(0xFFF4F6F9),
            surface = SurfaceWhite
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                HeaderBar()

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier

                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        tonalElevation = 6.dp,
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp, bottom = 30.dp),
                        color = SurfaceWhite
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            // Title
                            Text(
                                text = "Call Recordings",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TitleText,
                                modifier = Modifier.padding(start = 18.dp, top = 18.dp, bottom = 12.dp)
                            )

                            Divider()

                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                // Recording items
                                items(recordingItems) { rec ->
                                    RecordingRow(rec)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                                item { Spacer(modifier = Modifier.height(28.dp)) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderBar() {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp
    val headerHeight = (screenHeight * 0.16).dp.coerceAtLeast(80.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(headerHeight)
            .background(
                PrimaryBlue,
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            Spacer(modifier = Modifier.width(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Call, contentDescription = "App", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text("DigiDial", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
            }
        }
    }
}

@Composable
private fun RecordingRow(rec: RecordingItem) {
    var showDialog by remember { mutableStateOf(false) }

    // Card surface
    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        color = ItemCardGray,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true } // 🔥 Card tap
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Call,
                    contentDescription = "call",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    rec.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = TitleText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(rec.subTitle, fontSize = 12.sp, color = Color.Gray)
            }

            IconButton(onClick = { /* play recording */ }) {
                Icon(Icons.Default.GraphicEq, contentDescription = "waveform", tint = PrimaryBlue)
            }
        }
    }

    // 🔥 Dialog on tap
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Close")
                }
            },
            title = { Text(text = "Recording Details") },
            text = {
                Column {
                    Text("Caller: ${rec.title}")
                    Text("Time: ${rec.subTitle}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Duration: 2 min 35 sec")
                    Text("Status: Completed")
                }
            }
        )
    }
}

@Composable
private fun NotificationRow(notif: NotificationItem, switchState: Boolean, onSwitchChange: (Boolean) -> Unit) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        color = ItemCardGray,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Email, contentDescription = "mail", tint = PrimaryBlue, modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(notif.title, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, color = TitleText)
                Spacer(modifier = Modifier.height(4.dp))
//                Text(notif.subTitle, fontSize = 12.sp, color = Color.Gray)
            }

            Switch(
                checked = switchState,
                onCheckedChange = { onSwitchChange(it) }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun CallRecordingScreenPreview() {
    CallRecordingsScreen(navController = rememberNavController())
}