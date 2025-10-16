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

// Data class for a single client proposal
data class ClientProposal(
    val id: String = UUID.randomUUID().toString(),
    var clientName: String,
    var proposalTitle: String,
    var proposalContent: String,
    var timestamp: Long = System.currentTimeMillis()
)

// Repository for managing proposals using SharedPreferences
class ClientProposalRepository(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("client_proposals_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val proposalsKey = "proposals_list"

    fun saveProposals(proposals: List<ClientProposal>) {
        val json = gson.toJson(proposals)
        sharedPreferences.edit().putString(proposalsKey, json).apply()
    }

    fun loadProposals(): MutableList<ClientProposal> {
        val json = sharedPreferences.getString(proposalsKey, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<ClientProposal>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientProposalScreen(navController: NavController) {
    val context = LocalContext.current
    val proposalRepository = remember { ClientProposalRepository(context) }

    val proposals = remember { mutableStateListOf<ClientProposal>().apply { addAll(proposalRepository.loadProposals()) } }
    
    var clientName by rememberSaveable { mutableStateOf("") }
    var proposalTitle by rememberSaveable { mutableStateOf("") }
    var proposalContent by rememberSaveable { mutableStateOf("") }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF87CEEB), Color.White)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Client Proposals", fontWeight = FontWeight.Bold) },
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
                    if (clientName.isNotBlank() && proposalTitle.isNotBlank() && proposalContent.isNotBlank()) {
                        val newProposal = ClientProposal(
                            clientName = clientName,
                            proposalTitle = proposalTitle,
                            proposalContent = proposalContent
                        )
                        proposals.add(0, newProposal)
                        proposalRepository.saveProposals(proposals)
                        
                        clientName = ""
                        proposalTitle = ""
                        proposalContent = ""
                        
                        scope.launch {
                            snackbarHostState.showSnackbar("Proposal saved successfully!")
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Please fill all fields.")
                        }
                    }
                },
                containerColor = Color(0xFF2196F3)
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save Proposal", tint = Color.White)
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
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedLabelColor = Color(0xFF2196F3),
                            unfocusedLabelColor = Color.DarkGray
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = proposalTitle,
                        onValueChange = { proposalTitle = it },
                        label = { Text("Proposal Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedLabelColor = Color(0xFF2196F3),
                            unfocusedLabelColor = Color.DarkGray
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = proposalContent,
                        onValueChange = { proposalContent = it },
                        label = { Text("Proposal Details") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        maxLines = 10,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedLabelColor = Color(0xFF2196F3),
                            unfocusedLabelColor = Color.DarkGray
                        )
                    )
                }
            }
            
            Text(
                text = "Recent Proposals",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (proposals.isEmpty()) {
                    item {
                        Text(
                            "No proposals found.", 
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray
                        )
                    }
                } else {
                    items(proposals) { proposal ->
                        ProposalItem(proposal = proposal)
                    }
                }
            }
        }
    }
}

@Composable
fun ProposalItem(proposal: ClientProposal) {
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
                text = proposal.proposalTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "For: ${proposal.clientName}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = proposal.proposalContent,
                fontSize = 14.sp,
                color = Color.Gray,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Created on: ${java.text.SimpleDateFormat("MMM dd, yyyy - hh:mm a").format(java.util.Date(proposal.timestamp))}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClientProposalScreenPreview() {
    ClientProposalScreen(navController = rememberNavController())
}
