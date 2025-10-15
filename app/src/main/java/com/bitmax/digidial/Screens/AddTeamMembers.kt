package com.bitmax.digidial.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.util.UUID

// Data class for a single team member
data class TeamMember(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var number: String
)

// Repository for managing team members using SharedPreferences
class TeamMemberRepository(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("team_members_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val membersKey = "team_members_list"

    fun saveMembers(members: List<TeamMember>) {
        val json = gson.toJson(members)
        sharedPreferences.edit().putString(membersKey, json).apply()
    }

    fun loadMembers(): MutableList<TeamMember> {
        val json = sharedPreferences.getString(membersKey, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<TeamMember>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTeamMembersScreen(navController: NavController) {
    val context = LocalContext.current
    val memberRepository = remember { TeamMemberRepository(context) }

    var name by rememberSaveable { mutableStateOf("") }
    var number by rememberSaveable { mutableStateOf("") }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFF87CEEB), Color.White)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Team Member", fontWeight = FontWeight.Bold) },
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
                    if (name.isNotBlank() && number.isNotBlank()) {
                        val members = memberRepository.loadMembers()
                        val newMember = TeamMember(name = name, number = number)
                        members.add(newMember)
                        memberRepository.saveMembers(members)
                        
                        scope.launch {
                            snackbarHostState.showSnackbar("Member saved successfully!")
                        }
                        scope.launch {
                            kotlinx.coroutines.delay(1000)
                            navController.navigateUp()
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Name and number cannot be empty.")
                        }
                    }
                },
                containerColor = Color(0xFF2196F3)
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save Member", tint = Color.White)
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
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Member Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = number,
                        onValueChange = { number = it },
                        label = { Text("Member Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddTeamMembersScreenPreview() {
    AddTeamMembersScreen(navController = rememberNavController())
}
