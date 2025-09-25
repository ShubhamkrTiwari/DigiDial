package com.bitmax.digidial.Screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Mic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun RecordingScreen(navController: NavController,
                    onBack: () -> Unit = {},
                    onStartCall: () -> Unit = {}
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFF2F4F7)) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Blue bar with back arrow and title
            TopAppBar(
                title = { Text("Call Recordings", fontSize = 18.sp, fontWeight = FontWeight.Medium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                backgroundColor = Color(0xFF2F80ED),
                contentColor = Color.White,
                elevation = 2.dp,
                modifier = Modifier.height(100.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(500.dp)
                        .heightIn(min = 60.dp),
                    shape = RoundedCornerShape(10.dp),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .size(400.dp)
                            .padding(60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Mic artwork
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEFF6FF))
                        ) {
                            Canvas(modifier = Modifier.matchParentSize()) {
                                val center = this.size.center
                                val waveColor = Color(0xFF79A9FF)
                                val radius = size.minDimension / 3f
                                drawArc(
                                    color = waveColor,
                                    startAngle = -60f,
                                    sweepAngle = 60f,
                                    useCenter = false,
                                    topLeft = Offset(center.x - radius - 8f, center.y - radius / 2),
                                    size = androidx.compose.ui.geometry.Size(radius, radius),
                                    style = Stroke(width = 3f, cap = StrokeCap.Round)
                                )
                                drawArc(
                                    color = waveColor,
                                    startAngle = 180f,
                                    sweepAngle = 60f,
                                    useCenter = false,
                                    topLeft = Offset(center.x + 8f, center.y - radius / 2),
                                    size = androidx.compose.ui.geometry.Size(radius, radius),
                                    style = Stroke(width = 3f, cap = StrokeCap.Round)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Color.White),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Mic",
                                    tint = Color(0xFF2F80ED),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "No Recordings Yet",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF222222)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Your call recordings will appear here\nafter your first business call",
                            fontSize = 12.sp,
                            color = Color(0xFF7D8590),
                            modifier = Modifier.padding(horizontal = 12.dp),
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Bottom button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp, bottom = 50.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onStartCall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF2F80ED)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Start New Call",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
