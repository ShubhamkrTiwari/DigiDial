package com.bitmax.digidial.Screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bitmax.digidial.Models.CallRequest
import com.bitmax.digidial.Models.CallResponse
import com.bitmax.digidial.network.ApiClient
import com.apoorv.myvoice.socket.MicStreamer
import com.apoorv.myvoice.socket.TwilioAudioSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                    modifier = Modifier.fillMaxSize().background(Color.White)
                ) {
                    TabSection(selectedTab) { selectedTab = it }
                    CallHistorySection(selectedTab)
                    Spacer(modifier = Modifier.height(16.dp))
                    MakeFirstCallButton { showDialDialog = true }
                }

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
            IconButton(onClick = { }) {
                Icon(Icons.Outlined.SupportAgent, contentDescription = "Contact Support", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryBlue)
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
                Modifier.tabIndicatorOffset(tabPositions[selectedTab]).height(3.dp),
                color = PrimaryBlue
            )
        }
    ) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = { Text(title, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) },
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
                Box(modifier = Modifier.fillMaxSize().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text("No calls found", color = Color.Gray, fontSize = 16.sp)
                }
            }
        } else {
            items(filteredCalls) { call ->
                CallItem(call.name, call.time, call.label, call.missed)
                Divider(color = Color.LightGray.copy(alpha = 0.3f))
            }
        }
    }
}

@Composable
fun CallItem(name: String, time: String, label: String? = null, missed: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
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
                label?.let { Text(it, color = PrimaryBlue, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
            }
        }
        if (missed) Text("Missed", color = Color.Red, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun MakeFirstCallButton(onShowDialer: () -> Unit) {
    Button(
        onClick = onShowDialer,
        modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
    ) {
        Text("MAKE YOUR FIRST CALL 😊", fontWeight = FontWeight.Bold, color = Color.White)
    }
}

// ---------------- In-App Dialer with Audio ----------------
@Composable
fun InAppDialer(onDismiss: () -> Unit) {
    var input by remember { mutableStateOf("") }
    var isCallActive by remember { mutableStateOf(false) }

    // State for socket and mic streamer
    var audioSocket by remember { mutableStateOf<TwilioAudioSocket?>(null) }
    var micStreamer by remember { mutableStateOf<MicStreamer?>(null) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Clean up resources when the dialog is dismissed
    DisposableEffect(Unit) {
        onDispose {
            Log.d("InAppDialer", "Disposing Dialer. Cleaning up resources.")
            micStreamer?.stopStreaming()
            audioSocket?.disconnect()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d("InAppDialer", "Mic permission granted. Starting stream.")
            micStreamer?.startStreaming()
        } else {
            Toast.makeText(context, "⚠️ Microphone permission is required to speak.", Toast.LENGTH_LONG).show()
        }
    }

    val startCallFlow = {
        triggerCall(input, context,
            onSuccess = {
                Log.d("InAppDialer", "API call successful. Setting up audio stream.")
                isCallActive = true

                scope.launch(Dispatchers.IO) {
                    // Initialize and connect the socket
                    val socket = TwilioAudioSocket("wss://supernova-5aag.onrender.com/audio-stream")
                    audioSocket?.disconnect()
                    audioSocket?.let { audioSocket } ?: socket
                    socket.connect()
                    delay(1000)// Wait for connection to establish

                    if (socket.isConnected()) {
                        withContext(Dispatchers.Main) {
                            Log.d("InAppDialer", "Socket connected. Initializing mic streamer.")
                            // Initialize the streamer
                            micStreamer = MicStreamer(socket)

                            // Check permission and start streaming
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                micStreamer?.startStreaming()
                            } else {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        }
                    } else {
                        Log.e("InAppDialer", "Socket connection failed!")
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Socket connection failed!", Toast.LENGTH_SHORT).show()
                            isCallActive = false // Reset state
                        }
                    }
                }
            },
            onFailure = {
                Log.e("InAppDialer", "API call failed.")
                isCallActive = false
            }
        )
    }

    AlertDialog(
        onDismissRequest = {
            if (!isCallActive) onDismiss()
        },
        title = { Text(if (isCallActive) "📞 Call in Progress..." else "Dialer") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(input.ifEmpty { "Enter number" }, fontSize = 26.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
                Spacer(modifier = Modifier.height(12.dp))

                if (!isCallActive) {
                    val buttons = listOf(listOf("1","2","3"), listOf("4","5","6"), listOf("7","8","9"), listOf("*","0","#"))
                    buttons.forEach { row ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            row.forEach { digit ->
                                OutlinedButton(onClick = { input += digit }, modifier = Modifier.size(70.dp).padding(4.dp), shape = CircleShape) {
                                    Text(digit, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!isCallActive) {
                        OutlinedButton(onClick = { if (input.isNotEmpty()) input = input.dropLast(1) }, modifier = Modifier.size(70.dp), shape = CircleShape) {
                             Icon(Icons.Default.Backspace, contentDescription = "Delete")
                        }
                        Button(
                            onClick = {
                                if (input.isNotEmpty()) startCallFlow() else Toast.makeText(context, "Please enter a number", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(70.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.White)
                        }
                    } else {
                        Button(
                            onClick = {
                                micStreamer?.stopStreaming()
                                audioSocket?.disconnect()
                                isCallActive = false
                                Toast.makeText(context,"Call ended",Toast.LENGTH_SHORT).show()
                            },
                            modifier=Modifier.size(70.dp),
                            shape=CircleShape,
                            colors=ButtonDefaults.buttonColors(containerColor = Color.Red)
                        ) {
                            Icon(Icons.Default.CallEnd, contentDescription = "End Call", tint = Color.White)
                        }
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            if (!isCallActive) {
                TextButton(onClick = onDismiss) { Text("Close") }
            }
        }
    )
}

// No longer a suspend function
private fun triggerCall(toNumber: String, context: Context, onSuccess: () -> Unit, onFailure: () -> Unit) {
    val request = CallRequest(
        team_id = 18, // Replace with dynamic team_id if available
        call_ssid = "call_" + System.currentTimeMillis(),
        from_number = "+12183773645", // Replace with dynamic from_number if available
        to_number = if(toNumber.startsWith("+")) toNumber else "+91$toNumber",
        status = "Queued"
    )

    Log.d("triggerCall", "Initiating call to: ${request.to_number}")

    ApiClient.authApi.createCall(request).enqueue(object : Callback<CallResponse> {
        override fun onResponse(call: Call<CallResponse>, response: Response<CallResponse>) {
            if (response.isSuccessful && response.body() != null) {
                Log.d("triggerCall", "Call API success: ${response.body()}")
                onSuccess()
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                Log.e("triggerCall", "Call API failed with code ${response.code()}: $errorBody")
                Toast.makeText(context, "Call initiation failed: ${response.message()}", Toast.LENGTH_LONG).show()
                onFailure()
            }
        }

        override fun onFailure(call: Call<CallResponse>, t: Throwable) {
            Log.e("triggerCall", "Network error: ${t.message}", t)
            Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            onFailure()
        }
    })
}

@Preview(showBackground = true)
@Composable
fun CompanyDetailsPreview() {
    CallScreenUI(navController = rememberNavController())
}
