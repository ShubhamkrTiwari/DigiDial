package com.bitmax.digidial.Screens


import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.SupportAgent
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.ExperimentalMaterial3Api

val PrimaryBlue = Color(0xFF1976D2)
val LightBlue = Color(0xFFE3F2FD)

data class CallData(
    val name: String,
    val time: String,
    val missed: Boolean = false,
    val label: String? = null,
    val myCall: Boolean = false,
    val callBack: Boolean = false
)


@Composable
fun CallScreenUI(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    var showDialDialog by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = PrimaryBlue,
            secondary = LightBlue,
            background = Color.White,
            surface = Color.White
        )
    ) {
        Scaffold(
            topBar = { TopBanner() },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDialDialog = true },
                    containerColor = PrimaryBlue
                ) {
                    Icon(Icons.Default.Dialpad, contentDescription = "Dialer", tint = Color.White)
                }
            }
        ) { padding ->
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                ) {
                    TabSection(selectedTab) { selectedTab = it }
                    CallHistorySection(selectedTab)
                    Spacer(modifier = Modifier.height(16.dp))
                    MakeFirstCallButton()
                }

                // Custom In-App Dialer
                if (showDialDialog) {
                    InAppDialer(onDismiss = { showDialDialog = false })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBanner() {
    TopAppBar(
        title = { Text("Calls", color = Color.White) },
        actions = {
            IconButton(onClick = { /* TODO: Contact support */ }) {
                Icon(Icons.Outlined.SupportAgent, contentDescription = "Contact Support", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PrimaryBlue
        )
    )
}

@Composable
fun TabSection(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("All Calls", "My Calls", "Call Back")

    TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White,
        contentColor = PrimaryBlue,
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTab])
                    .height(3.dp),
                color = PrimaryBlue
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        title,
                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selectedContentColor = PrimaryBlue,
                unselectedContentColor = Color.Gray
            )
        }
    }
}

@Composable
fun CallHistorySection(selectedTab: Int) {
    val allCalls = listOf(
        CallData("+917536861853", "06:18 pm (2)", missed = false, myCall = true),
        CallData("+919634607848", "06:17 pm", missed = false),
        CallData("Superfone Sales", "10:06 am • 18 Sept", label = "Demo", missed = true, callBack = true),
        CallData("+918534504487", "07:18 pm (2)", missed = false, myCall = true),
        CallData("+919719504487", "08:17 pm", missed = false),
        CallData("Superfone Sales", "11:06 am • 18 Sept", label = "Demo", missed = true, callBack = true)
    )

    val filteredCalls = when (selectedTab) {
        0 -> allCalls
        1 -> allCalls.filter { it.myCall }
        2 -> allCalls.filter { it.callBack }
        else -> allCalls
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        if (filteredCalls.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No calls found", color = Color.Gray, fontSize = 16.sp)
                }
            }
        } else {
            items(filteredCalls) { call ->
                CallItem(
                    name = call.name,
                    time = call.time,
                    label = call.label,
                    missed = call.missed
                )
                Divider(color = Color.LightGray.copy(alpha = 0.3f))
            }
        }
    }
}

@Composable
fun CallItem(name: String, time: String, label: String? = null, missed: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (missed) Icons.Default.CallMissed else Icons.Default.CallMade,
                contentDescription = null,
                tint = if (missed) Color.Red else PrimaryBlue,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(time, color = Color.Gray, fontSize = 13.sp)
                label?.let {
                    Text(it, color = PrimaryBlue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        if (missed) {
            Text("Missed", color = Color.Red, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun MakeFirstCallButton() {
    var showDialer by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialer = true },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Text("MAKE YOUR FIRST CALL 😊", fontWeight = FontWeight.Bold, color = Color.White)
    }

    if (showDialer) {
        InAppDialer(onDismiss = { showDialer = false })
    }
}

// ------------------- Custom In-App Dialer -------------------
@Composable
fun InAppDialer(onDismiss: () -> Unit) {
    var input by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dialer") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Number Display
                Text(
                    text = input.ifEmpty { "Enter number" },
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Keypad Layout
                val buttons = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("*", "0", "#")
                )

                buttons.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        row.forEach { digit ->
                            OutlinedButton(
                                onClick = { input += digit },
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(4.dp),
                                shape = CircleShape
                            ) {
                                Text(digit, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Backspace & Call buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { if (input.isNotEmpty()) input = input.dropLast(1) },
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape
                    ) {
                        Icon(Icons.Default.Backspace, contentDescription = "Delete")
                    }

                    Button(
                        onClick = {
                            if (input.isNotEmpty()) {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:$input")
                                }
                                context.startActivity(intent)
                                onDismiss()
                            }
                        },
                        modifier = Modifier.size(80.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
@Preview(showBackground = true)
@Composable
fun CompanyDetailsPreview() {
    CallScreenUI(navController = rememberNavController())
}

