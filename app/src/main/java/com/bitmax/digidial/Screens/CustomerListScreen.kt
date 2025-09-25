package com.bitmax.digidial.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bitmax.digidial.R

// ✅ Data model
data class Customer(
    val id: Int,
    val name: String,
    val phone: String = "N/A",
    val email: String = "N/A",
    val lastCall: String,
    val isNew: Boolean = false,
    val type: String = "All", // "Lead", "VIP", or "All"
    val callHistory: List<String> = emptyList()
)


@Composable
fun CustomerListScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("All", "Leads", "VIP")


    // ✅ Search query state
    var searchQuery by remember { mutableStateOf("") }

    // ✅ Customer list state (mutable for adding new ones)
    var customers by remember {
        mutableStateOf(
            listOf(
                Customer(
                    1,
                    "Michael Johnson",
                    "9876543210",
                    "michael@mail.com",
                    "3 days ago",
                    callHistory = listOf("Called on 17 Sep", "Called on 14 Sep")
                ),
                Customer(
                    2,
                    "Sarah Chen",
                    "9876543211",
                    "sarah@mail.com",
                    "Today, 10:30 AM",
                    isNew = true,
                    type = "Lead",
                    callHistory = listOf("Called Today")
                ),
                Customer(
                    3,
                    "David Lee",
                    "9876543212",
                    "david@mail.com",
                    "2 days ago",
                    type = "VIP",
                    callHistory = listOf("Called 2 days ago")
                ),
                Customer(
                    4,
                    "Emily Davis",
                    "8967898967",
                    "emily@gmail.com",
                    "4 days ago",
                    type = "Lead",
                    callHistory = listOf("Called 4 days ago")
                ),
                Customer(
                    1,
                    "Michael Johnson",
                    "9876543210",
                    "michael@mail.com",
                    "3 days ago",
                    callHistory = listOf("Called on 17 Sep", "Called on 14 Sep")
                ),
                Customer(
                    2,
                    "Sarah Chen",
                    "9876543211",
                    "sarah@mail.com",
                    "Today, 10:30 AM",
                    isNew = true,
                    type = "Lead",
                    callHistory = listOf("Called Today")
                ),
                Customer(
                    3,
                    "David Lee",
                    "9876543212",
                    "david@mail.com",
                    "2 days ago",
                    type = "VIP",
                    callHistory = listOf("Called 2 days ago")
                ),
                Customer(
                    4,
                    "Emily Davis",
                    "8967898967",
                    "emily@gmail.com",
                    "4 days ago",
                    type = "Lead",
                    callHistory = listOf("Called 4 days ago")
                ),
                Customer(
                    1,
                    "Michael Johnson",
                    "9876543210",
                    "michael@mail.com",
                    "3 days ago",
                    callHistory = listOf("Called on 17 Sep", "Called on 14 Sep")
                ),
                Customer(
                    2,
                    "Sarah Chen",
                    "9876543211",
                    "sarah@mail.com",
                    "Today, 10:30 AM",
                    isNew = true,
                    type = "Lead",
                    callHistory = listOf("Called Today")
                ),
                Customer(
                    3,
                    "David Lee",
                    "9876543212",
                    "david@mail.com",
                    "2 days ago",
                    type = "VIP",
                    callHistory = listOf("Called 2 days ago")
                ),
                Customer(
                    4,
                    "Emily Davis",
                    "8967898967",
                    "emily@gmail.com",
                    "4 days ago",
                    type = "Lead",
                    callHistory = listOf("Called 4 days ago")
                )
            )
        )
    }


    var selectedCustomer by remember { mutableStateOf<Customer?>(null) }
    var showAddDialog by remember { mutableStateOf(false) }

    // 🔹 Tab + Search Filter Logic
    val filteredCustomers = customers.filter { customer ->
        val matchesTab = when (selectedTab) {
            1 -> customer.type == "Lead"
            2 -> customer.type == "VIP"
            else -> true
        }
        val matchesSearch = customer.name.contains(searchQuery, ignoreCase = true) ||
                customer.lastCall.contains(searchQuery, ignoreCase = true)
        matchesTab && matchesSearch
    }.sortedBy { it.name } // Alphabetical order

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp)
                .background(Color(0xFFE8F2FF))
        ) {
            // 🔹 Top Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF3399FF))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.superone), // App Logo
                            contentDescription = "Logo",
                            modifier = Modifier.size(70.dp)
                        )
                        Spacer(modifier = Modifier.width(45.dp))
                        Text(
                            text = "Superfone",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                    }
                }

                // 🔹 SearchBar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search customers...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                )
            }

            // 🔹 Title
            Text(
                text = "Contacts",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp)
            )

            // 🔹 Scrollable Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = Color(0xFF007BFF),
                edgePadding = 8.dp,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.Black,
                        text = {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (selectedTab == index) Color(0xFF007BFF)
                                        else Color(0xFFF2F2F2)
                                    )
                                    .padding(horizontal = 24.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = title,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = if (selectedTab == index) Color.White else Color.Black
                                )
                            }
                        }
                    )
                }
            }

            // 🔹 Customer List
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                if (filteredCustomers.isEmpty()) {
                    item {
                        Text(
                            text = "No customers found",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray
                        )
                    }
                } else {
                    items(filteredCustomers) { customer ->
                        CustomerItem(
                            customer = customer,
                            onClick = { selectedCustomer = customer }
                        )
                    }
                }
            }
        }

        // 🔹 Floating Add Button
        FloatingActionButton(
            onClick = { showAddDialog = true },
            containerColor = Color(0xFF007BFF),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(40.dp, bottom = 65.dp, end = 20.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
        }

        // 🔹 Customer Detail Dialog
        selectedCustomer?.let { customer ->
            AlertDialog(
                onDismissRequest = { selectedCustomer = null },
                title = {
                    Text(customer.name, fontWeight = FontWeight.Bold)
                },
                text = {
                    // Wrap content in a Box to control width
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f) // 90% of screen width
                            .verticalScroll(rememberScrollState())
                    ) {
                        Column {
                            Text("Phone: ${customer.phone}")
                            Text("Email: ${customer.email}")
                            Text("Last Call: ${customer.lastCall}")
                            Text("Type: ${customer.type}")
                            if (customer.isNew) Text("Status: New Customer", color = Color(0xFF007BFF))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Call History:", fontWeight = FontWeight.SemiBold)
                            customer.callHistory.forEach { call ->
                                Text("- $call", fontSize = 13.sp, color = Color.Gray)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row {
                                Button(
                                    onClick = { /* TODO: Start call */ },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
                                ) { Text("Call", color = Color.White) }
                                Spacer(modifier = Modifier.width(12.dp))
                                Button(onClick = { /* TODO: Open chat */ }) { Text("Chat") }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedCustomer = null }) {
                        Text("Close", color = Color(0xFF007BFF))
                    }
                }
            )
        }

        // 🔹 Add Customer Dialog
        if (showAddDialog) {
            var name by remember { mutableStateOf("") }
            var lastCall by remember { mutableStateOf("") }
            var type by remember { mutableStateOf("All") }
            var isNew by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Add New Customer") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = lastCall,
                            onValueChange = { lastCall = it },
                            label = { Text("Last Call") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Type selection
                        Row {
                            listOf("All", "Lead", "VIP").forEach { option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(end = 12.dp)
                                        .clickable { type = option }
                                ) {
                                    RadioButton(
                                        selected = type == option,
                                        onClick = { type = option })
                                    Text(option)
                                }
                            }
                        }

                        // New customer checkbox
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isNew, onCheckedChange = { isNew = it })
                            Text("Mark as New")
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (name.isNotBlank()) {
                            val newCustomer = Customer(
                                id = customers.size + 1,
                                name = name,
                                lastCall = lastCall.ifBlank { "N/A" },
                                isNew = isNew,
                                type = type
                            )
                            customers = customers + newCustomer
                            showAddDialog = false
                        }
                    }) {
                        Text("Add", color = Color(0xFF007BFF))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}


@Composable
fun CustomerItem(customer: Customer, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        customer.name.first().toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        customer.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(customer.lastCall, fontSize = 13.sp, color = Color.Gray)
                }

                if (customer.isNew) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF007BFF))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("New", color = Color.White, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}





