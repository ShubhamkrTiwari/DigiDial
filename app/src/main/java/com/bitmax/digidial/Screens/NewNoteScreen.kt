package com.bitmax.digidial.Screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Menu
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

// Note data class with a unique ID and mutable fields
data class Note(
    val id: String = UUID.randomUUID().toString(),
    var title: String,
    var content: String
)

// Repository to handle data operations using SharedPreferences
class NoteRepository(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val notesKey = "notes_list"

    fun saveNotes(notes: List<Note>) {
        val json = gson.toJson(notes)
        sharedPreferences.edit().putString(notesKey, json).apply()
    }

    fun loadNotes(): MutableList<Note> {
        val json = sharedPreferences.getString(notesKey, null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Note>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf(Note(title = "Welcome Note", content = "This is a sample note."))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewNoteScreen(navController: NavController) {
    val context = LocalContext.current
    val noteRepository = remember { NoteRepository(context) }

    val notes = remember { mutableStateListOf<Note>().apply { addAll(noteRepository.loadNotes()) } }

    var activeNoteId by rememberSaveable { mutableStateOf<String?>(notes.firstOrNull()?.id) }
    var title by remember { mutableStateOf(notes.find { it.id == activeNoteId }?.title ?: "") }
    var content by remember { mutableStateOf(notes.find { it.id == activeNoteId }?.content ?: "") }

    // This effect runs when activeNoteId changes to load the selected note.
    LaunchedEffect(activeNoteId) {
        val note = notes.find { it.id == activeNoteId }
        title = note?.title ?: ""
        content = note?.content ?: ""
    }

    // This effect auto-saves the active note when its title or content changes.
    LaunchedEffect(title, content) {
        activeNoteId?.let { id ->
            val noteToUpdate = notes.find { it.id == id }
            noteToUpdate?.let {
                if (it.title != title || it.content != content) {
                    it.title = title
                    it.content = content
                    noteRepository.saveNotes(notes)
                }
            }
        }
    }

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFE8F2FF), Color(0xFFC8D9FF))
    )

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                Text("Your Notes", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), style = MaterialTheme.typography.titleMedium)
                Divider()
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "New Note") },
                    label = { Text("New Note") },
                    selected = activeNoteId == null,
                    onClick = {
                        activeNoteId = null
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                Divider()
                LazyColumn {
                    items(notes) { note ->
                        NavigationDrawerItem(
                            label = { Text(note.title) },
                            selected = note.id == activeNoteId,
                            onClick = {
                                activeNoteId = note.id
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            }
        }
    ) {
        Box(modifier = Modifier.fillMaxSize().background(gradientBackground)) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = { },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
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
                            if (activeNoteId == null && title.isNotBlank()) {
                                val newNote = Note(title = title, content = content)
                                notes.add(0, newNote)
                                noteRepository.saveNotes(notes)
                                activeNoteId = newNote.id
                            }
                            scope.launch { drawerState.open() }
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
                            ),
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
                        )
                    }
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
