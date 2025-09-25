package com.bitmax.digidial.Screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)

@Composable

fun HomeScreen(navController: NavController){
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFE8F2FF))
                .verticalScroll(scrollState)
        ) {
            // 🔹 TopBar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Superfone",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )
                ProfileIcon(navController)
            }

            // 🔹 Productivity Hub
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { /* TODO Open productivity hub */ },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1976D2)),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("PRODUCTIVITY HUB", color = Color.White, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("NEXT MEETING: Project Sync - 11 AM", color = Color.White, fontSize = 14.sp)
                    Text("TASKS DUE TODAY: 5", color = Color.White, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("New Emails: 12", color = Color.White, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Quick Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionCard(Icons.Default.Call, "Quick Call")
                QuickActionCard(Icons.Default.NoteAdd, "New Note")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionCard(Icons.Default.Folder, "Project X")
                QuickActionCard(Icons.Default.BarChart, "Analytics")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Recent Files
            Text(
                text = "Recent Files",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FileCard("Q3 Report.pdf", Icons.Default.PictureAsPdf)
                FileCard("Meeting_Notes.docx", Icons.Default.Description)
                FileCard("Client_Proposal", Icons.Default.InsertDriveFile)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Module Overview
            Text(
                text = "Module Overview",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ModuleCard(Icons.Default.Person, "CRM", "New Lead: John Smith\nOpportunities: +5")
                    ModuleCard(Icons.Default.DateRange, "Calendar", "Next: Team Sync - 2PM\n5 events today")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ModuleCard(Icons.Default.Message, "Communications", "New Messages: 3")
                    ModuleCard(Icons.Default.Cloud, "Cloud Storage", "Missed Calls: 1")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 System Health
            Text(
                text = "System Health",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp)
            )

            SystemHealthItem("Battery", 0.86f)   // 86%
            SystemHealthItem("Storage", 0.75f)   // 75%

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Daily Digest
            Text("DAILY DIGEST", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(16.dp, 20.dp, 16.dp, 8.dp))

            DigestCard(Icons.Default.BarChart, "Activity: 125% of goal")
            DigestCard(Icons.Default.Star, "Top Contact: Sarah Chen", "(5 interactions)", Icons.Default.Cloud)
            DigestCard(Icons.Default.Star, "Top Contact: Sarh 3h 30m", "(interactions)", Icons.Default.AccessTime)



            // 🔹 Smart Suggestions
            Text("SMART SUGGESTIONS", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(16.dp, 20.dp, 16.dp, 8.dp))
            SuggestionCard("David Chen", "Tech Solutions") { /* TODO Call action */ }


            AppFooter() // 👈 Footer
        }
    }
}




// ✅ Quick Action Card
@Composable
fun QuickActionCard(icon: ImageVector, label: String) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(110.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF1976D2).copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}


// ✅ File Card
@Composable
fun FileCard(name: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .width(110.dp)
            .height(95.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFF1976D2).copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                color = Color(0xFF444444)
            )
        }
    }
}

@Composable
fun ModuleCard(icon: ImageVector, title: String, subtitle: String) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFE3F2FD), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(subtitle, fontSize = 12.sp, color = Color.Gray, maxLines = 2)
            }
        }
    }
}

@Composable
fun SystemHealthItem(label: String, progress: Float) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontWeight = FontWeight.SemiBold)
            Text("${(progress * 100).toInt()}%", color = Color.Gray, fontSize = 12.sp)
        }
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Color(0xFF1976D2)
        )
    }
}

@Composable
fun DigestCard(icon: ImageVector, title: String, subtitle: String? = null, endIcon: ImageVector? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF1976D2).copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.Black)
                subtitle?.let {
                    Text(it, fontSize = 12.sp, color = Color.Gray)
                }
            }

            endIcon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}


@Composable
fun SuggestionCard(name: String, company: String, onCall: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF1976D2).copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.BarChart, contentDescription = null, tint = Color(0xFF1976D2))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Based on your recent calls,", fontSize = 13.sp, color = Color.Gray)
                    Text("$name from $company", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onCall,
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Initiate Call")
            }
        }
    }
}

// ✅ Footer Section
@Composable
fun AppFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "© 2024 Superfone Inc.",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "| Privacy",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.clickable { /* Open Privacy */ }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "| Terms",
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.clickable { /* Open Terms */ }
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Send Feedback",
                fontSize = 12.sp,
                color = Color(0xFF1976D2), // Blue link color
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { /* Open Feedback */ }
            )
        }
    }
}



// ✅ Bottom Navigation Bar
@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
            onClick = {navController.navigate("homedashboard")}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Contacts, contentDescription = "Contacts") },
            label = { Text("Contacts") },
            selected = false,
            onClick = {navController.navigate("customerlist")}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Call, contentDescription = "Calls") },
            label = { Text("Calls") },
            selected = false,
            onClick = {navController.navigate("call")}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "CallRecordings") },
            label = { Text("Recording") },
            selected = false,
            onClick = {navController.navigate("callrecording")}
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = "More") },
            label = { Text("More") },
            selected = false,
            onClick = {}
        )
    }
}


@Composable
fun ProfileIcon(navController: NavController) {
    Icon(
        imageVector = Icons.Default.Menu,
        contentDescription = "Profile",
        modifier = Modifier
            .size(40.dp)
            .clickable {
                navController.navigate("dashboard")
            },
        tint = Color.Gray
    )
}