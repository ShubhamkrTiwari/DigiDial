package com.bitmax.digidial.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Preview(showBackground = true, showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CallDetailsScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Call Details", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF6F9FC))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Caller Card ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_avtar),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color(0xFF2B9EFF), CircleShape)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Olivia Chen", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("(555) 127-6643", fontSize = 14.sp, color = Color(0xFF5C6672))
                        Text("Missed Call", fontSize = 13.sp, color = Color(0xFFE53935))
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("10 min", fontSize = 13.sp, color = Color(0xFF5C6672))
                        Text("Today, 2:30 PM", fontSize = 13.sp, color = Color(0xFF5C6672))
                    }
                }
            }

            // --- Action Buttons ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionButton(
                    icon = Icons.Default.Call,
                    text = "Call Back",
                    bgGradient = Brush.horizontalGradient(
                        listOf(Color(0xFF2B9EFF), Color(0xFF4A90E2))
                    )
                )
                ActionButton(
                    icon = Icons.Default.PersonAdd,
                    text = "Add Contact",
                    bgGradient = Brush.horizontalGradient(
                        listOf(Color(0xFF6C63FF), Color(0xFF8E7CFF))
                    )
                )
            }

            // --- Call Recording Section ---
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(5.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Call Recording",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF0F1722)
                    )
                    Spacer(Modifier.height(12.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Play button
                        IconButton(
                            onClick = { /*TODO play audio*/ },
                            modifier = Modifier
                                .size(50.dp)
                                .background(
                                    Brush.radialGradient(
                                        listOf(Color(0xFF2B9EFF), Color(0xFF1565C0))
                                    ),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(Modifier.width(12.dp))

                        // Fake waveform + progress
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(36.dp)
                                .background(
                                    brush = Brush.horizontalGradient(
                                        listOf(Color(0xFFB0C4DE), Color(0xFF90CAF9))
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        )
                        Spacer(Modifier.width(12.dp))

                        Text("0:05 / 0:10", fontSize = 13.sp, color = Color(0xFF5C6672))
                    }
                }
            }

            // --- Agent Notes Section ---
            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(5.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Agent Notes",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF0F1722)
                    )
                    Spacer(Modifier.height(8.dp))

                    var notes by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        placeholder = { Text("Write your notes here...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            disabledContainerColor = Color.White,
                            focusedIndicatorColor = Color(0xFF2B9EFF),
                            unfocusedIndicatorColor = Color(0xFFB0BEC5),
                            cursorColor = Color(0xFF2B9EFF)
                        )
                    )


                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {
                            // TODO: Save notes to DB or backend
                        },
                        modifier = Modifier.align(Alignment.End),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2B9EFF))
                    ) {
                        Text("Save", color = Color.White)
                    }
                }
            }
        }

        }
}

@Composable
fun ActionRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActionButton(
            icon = Icons.Default.Call,
            text = "Call",
            bgGradient = Brush.horizontalGradient(listOf(Color(0xFF2B9EFF), Color(0xFF4A90E2)))
        )
        ActionButton(
            icon = Icons.Default.Message,
            text = "Message",
            bgGradient = Brush.horizontalGradient(listOf(Color(0xFF00C853), Color(0xFF64DD17)))
        )
    }
}

// ✅ Modern Action Button
@Composable
fun RowScope.ActionButton(
    icon: ImageVector,
    text: String,
    bgGradient: Brush
) {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .weight(1f)
            .height(50.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgGradient, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Icon(icon, contentDescription = text, tint = Color.White)
                Spacer(Modifier.width(6.dp))
                Text(text, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}
