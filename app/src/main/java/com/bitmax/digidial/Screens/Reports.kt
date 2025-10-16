package com.bitmax.digidial.Screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.util.UUID

// Data class for a single report
data class Report(
    val id: String = UUID.randomUUID().toString(),
    var clientName: String,
    var reportTitle: String,
    var reportContent: String,
    var timestamp: Long = System.currentTimeMillis()
)

// Repository for managing reports using SharedPreferences
class ReportRepository(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("reports_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val reportsKey = "reports_list"

    fun saveReports(reports: List<Report>) {
        val json = gson.toJson(reports)
        sharedPreferences.edit().putString(reportsKey, json).apply()
    }

    fun loadReports(): MutableList<Report> {
        val json = sharedPreferences.getString(reportsKey, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Report>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(navController: NavController) {
    val context = LocalContext.current
    val reportRepository = remember { ReportRepository(context) }

    // State for the list of reports
    val reports = remember { mutableStateListOf<Report>().apply { addAll(reportRepository.loadReports()) } }
    
    // State for the report input fields
    var clientName by rememberSaveable { mutableStateOf("") }
    var reportTitle by rememberSaveable { mutableStateOf("") }
    var reportContent by rememberSaveable { mutableStateOf("") }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF87CEEB), Color.White)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Client Reports", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (clientName.isNotBlank() && reportTitle.isNotBlank() && reportContent.isNotBlank()) {
                        val newReport = Report(
                            clientName = clientName,
                            reportTitle = reportTitle,
                            reportContent = reportContent
                        )
                        reports.add(0, newReport)
                        reportRepository.saveReports(reports)
                        
                        // Clear fields
                        clientName = ""
                        reportTitle = ""
                        reportContent = ""
                        
                        scope.launch {
                            snackbarHostState.showSnackbar("Report saved successfully!")
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("All fields are required.")
                        }
                    }
                },
                containerColor = Color(0xFF2196F3)
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save Report", tint = Color.White)
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = Color.Transparent,
        modifier = Modifier.background(gradientBackground)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Form to create a new report
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("Client Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = reportTitle,
                        onValueChange = { reportTitle = it },
                        label = { Text("Report Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = reportContent,
                        onValueChange = { reportContent = it },
                        label = { Text("Report Details") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        maxLines = 8
                    )
                }
            }
            
            Text(
                text = "Recent Reports",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // List of saved reports
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (reports.isEmpty()) {
                    item {
                        Text(
                            "No reports found. Create one using the form above.", 
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray
                        )
                    }
                } else {
                    items(reports) { report ->
                        ReportItem(report = report)
                    }
                }
            }
        }
    }
}

@Composable
fun ReportItem(report: Report) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = report.reportTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "Client: ${report.clientName}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = report.reportContent,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Created on: ${java.text.SimpleDateFormat("MMM dd, yyyy - hh:mm a").format(java.util.Date(report.timestamp))}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReportsScreenPreview() {
    ReportsScreen(navController = rememberNavController())
}
