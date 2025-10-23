
package com.bitmax.digidial.Screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

// Define custom colors for the sky-white theme
val SkyBlue = Color(0xFF03A9F4)
val OffWhite = Color(0xFFFAFAFA)

data class Contact(val id: Int, val name: String, val number: String, val isAppContact: Boolean)

val dummyContacts = listOf(
    Contact(1, "Aarav Sharma", "+91 98765 43210", true),
    Contact(2, "Vivaan Singh", "+91 99887 76655", false),
    Contact(3, "Aditya Kumar", "+91 87654 32109", true),
    Contact(4, "Diya Gupta", "+91 76543 21098", false),
    Contact(5, "Ishaan Patel", "+91 65432 10987", true),
    Contact(6, "Ananya Reddy", "+91 98765 12345", true),
    Contact(7, "Rohan Mehta", "+91 87654 67890", false)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailsScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("App Contacts", "Phone Contacts")
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts", fontWeight = FontWeight.Bold, color = Color.Black) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Handle add contact */ },
                containerColor = SkyBlue
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Contact", tint = Color.White)
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = SkyBlue,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = SkyBlue
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title, fontWeight = FontWeight.SemiBold) },
                        selectedContentColor = SkyBlue,
                        unselectedContentColor = Color.Gray
                    )
                }
            }

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search by name...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                colors = TextFieldDefaults.run {
                    colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = SkyBlue,

                            )
                }
            )

            when (selectedTabIndex) {
                0 -> {
                    val filteredContacts = dummyContacts.filter {
                        it.isAppContact && it.name.contains(searchQuery, ignoreCase = true)
                    }
                    ContactList(contacts = filteredContacts)
                }
                1 -> PhoneContactContent(searchQuery = searchQuery)
            }
        }
    }
}

@Composable
fun PhoneContactContent(searchQuery: String) {
    val context = LocalContext.current
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }

    if (hasPermission) {
        val phoneContacts = remember { getPhoneContacts(context) }
        val filteredContacts = phoneContacts.filter {
            it.name.contains(searchQuery, ignoreCase = true)
        }
        ContactList(contacts = filteredContacts)
    } else {
        RequestPermissionContent {
            permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }
}

@Composable
fun RequestPermissionContent(onRequestPermission: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Permission Required", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "To show your phone contacts, please grant the contacts permission.",
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRequestPermission) {
                Text("Grant Permission")
            }
        }
    }
}

private fun getPhoneContacts(context: Context): List<Contact> {
    val contactsList = mutableListOf<Contact>()
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )

    cursor?.use {
        val idCol = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID)
        val nameCol = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numberCol = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while (it.moveToNext()) {
            val id = it.getLong(idCol).toInt()
            val name = it.getString(nameCol)
            val number = it.getString(numberCol)
            contactsList.add(Contact(id, name, number, isAppContact = false))
        }
    }
    return contactsList
}

@Composable
fun ContactList(contacts: List<Contact>) {
    if (contacts.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No contacts found.", fontSize = 18.sp, color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(contacts) { contact ->
                ContactItem(contact = contact)
            }
        }
    }
}

@Composable
fun ContactItem(contact: Contact) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = OffWhite),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Contact Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray.copy(alpha = 0.5f))
                    .padding(8.dp),
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = contact.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = contact.number, color = Color.Gray, fontSize = 14.sp)
            }
            IconButton(onClick = { /* TODO: Handle call */ }) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Call",
                    tint = SkyBlue
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ContactDetailsScreenPreview() {
    ContactDetailsScreen(navController = rememberNavController())
}
