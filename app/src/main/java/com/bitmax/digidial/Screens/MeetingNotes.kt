package com.bitmax.digidial.screens

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

// Data class for a single meeting note
data class MeetingNote(
    val id: String = UUID.randomUUID().toString(),
    var title: String,
    var content: String,
    var timestamp: Long = System.currentTimeMillis()
)

// Repository for managing meeting notes using SharedPreferences
class MeetingNoteRepository(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("meeting_notes_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val notesKey = "meeting_notes_list"

    fun saveNotes(notes: List<MeetingNote>) {
        val json = gson.toJson(notes)
        sharedPreferences.edit().putString(notesKey, json).apply()
    }

    fun loadNotes(): MutableList<MeetingNote> {
        val json = sharedPreferences.getString(notesKey, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<MeetingNote>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeetingNotesScreen(navController: NavController) {
    val context = LocalContext.current
    val noteRepository = remember { MeetingNoteRepository(context) }

    // State for the list of notes
    val notes = remember { mutableStateListOf<MeetingNote>().apply { addAll(noteRepository.loadNotes()) } }
    
    // State for the currently selected note's title and content
    var title by rememberSaveable { mutableStateOf("") }
    var content by rememberSaveable { mutableStateOf("") }
    
    // A snackbar host state to show messages
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFE0F7FA), Color(0xFFB2EBF2), Color(0xFF80DEEA))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meeting Notes", fontWeight = FontWeight.Bold) },
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
                    if (title.isNotBlank() && content.isNotBlank()) {
                        val newNote = MeetingNote(title = title, content = content)
                        notes.add(0, newNote) // Add to the top of the list
                        noteRepository.saveNotes(notes)
                        
                        // Clear fields for new note
                        title = ""
                        content = ""
                        
                        scope.launch {
                            snackbarHostState.showSnackbar("Note saved successfully!")
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Title and content cannot be empty.")
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Save, contentDescription = "Save Note", tint = Color.White)
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
            // Section to create a new note
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Note Title") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("What's on your mind?") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        maxLines = 8
                    )
                }
            }
            
            // "Recent Notes" Title
            Text(
                text = "Recent Notes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // List of existing notes
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (notes.isEmpty()) {
                    item {
                        Text(
                            "No meeting notes yet. Add one using the form above!", 
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray
                        )
                    }
                } else {
                    items(notes) { note ->
                        MeetingNoteItem(note = note)
                    }
                }
            }
        }
    }
}

@Composable
fun MeetingNoteItem(note: MeetingNote) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.8f))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = note.content,
                fontSize = 14.sp,
                color = Color.DarkGray,
                maxLines = 3
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Saved on: ${java.text.SimpleDateFormat("MMM dd, yyyy - hh:mm a").format(java.util.Date(note.timestamp))}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MeetingNotesScreenPreview() {
    MeetingNotesScreen(navController = rememberNavController())
}
