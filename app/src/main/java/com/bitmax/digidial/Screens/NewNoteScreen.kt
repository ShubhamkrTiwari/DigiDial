package com.bitmax.digidial.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNoteScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFE8F2FF), Color(0xFFC8D9FF))
    )

    Box(modifier = Modifier.fillMaxSize().background(gradientBackground)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.Black,
                        navigationIconContentColor = Color.Black
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        // TODO: Save note logic
                        navController.popBackStack()
                    },
                    containerColor = Color(0xFF1976D2)
                ) {
                    Icon(Icons.Default.Done, contentDescription = "Save Note", tint = Color.White)
                }
            }
        ) { innerPadding ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("Title", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Gray)) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black),
                        singleLine = true
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    TextField(
                        value = content,
                        onValueChange = { content = it },
                        placeholder = { Text("Write your note here...", style = TextStyle(fontSize = 16.sp, color = Color.Gray)) },
                        modifier = Modifier.fillMaxSize(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun NewNoteScreenPreview() {
    NewNoteScreen(navController = rememberNavController())
}