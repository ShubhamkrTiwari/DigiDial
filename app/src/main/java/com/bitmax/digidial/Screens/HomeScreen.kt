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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

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


            // ✅ New sections
            TrialAccountSection(navController)
            MyTeamSection(navController)

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Quick Actions
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QuickActionCard(Icons.Default.Call, "Quick Call") { navController.navigate("call") }
                QuickActionCard(Icons.Default.NoteAdd, "New Note") { navController.navigate("new_note") }
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
                    .padding(horizontal = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FileCard("Q3 Report.pdf", Icons.Default.PictureAsPdf)
                FileCard("Meeting_Notes.docx", Icons.Default.Description)
                FileCard("Client_Proposal", Icons.Default.InsertDriveFile)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 Module Overview




            Spacer(modifier = Modifier.height(20.dp))

            // 🔹 System Health


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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickActionCard(icon: ImageVector, label: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
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
                .padding(12.dp),
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


// ----------------- New UI Components -----------------
@Composable
fun TrialAccountSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2196F3))
            .padding(16.dp)
    ) {
        // 🔹 Top Row: Menu + Title + Help
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    // Navigate to Dashboard screen
                    navController.navigate("dashboard")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "DIGIDIAL",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            Icon(
                imageVector = Icons.Default.Help,
                contentDescription = "Help",
                tint = Color(0xFFFF9800),
                modifier = Modifier
                    .size(26.dp)
                    .clickable {
                        // Navigate to Help & Support screen
                        navController.navigate("helpandsupport")
                    }
            )
        }

        Spacer(Modifier.height(10.dp))

        // 🔹 Trial Account number + actions
        Text("TRIAL ACCOUNT", color = Color.Yellow, fontSize = 12.sp)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "+91 9429693249",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Row {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy",
                    tint = Color.White,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(end = 12.dp)
                )
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // 🔹 Boost Google Profile Card
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    tint = Color(0xFF4285F4),
                    modifier = Modifier.size(26.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    "Boost your Google Business Profile",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // 🔹 Trial Expiry Banner
        Card(
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Trial expires in 3 days", color = Color(0xFFE65100))
                Text(
                    "VIEW PLANS",
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun MyTeamSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("My Team", fontWeight = FontWeight.Bold, fontSize = 16.sp)

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TeamMemberCircle("9", "You", Color(0xFFFFCDD2))
            TeamMemberCircle("SK", "SHUBHA M", Color(0xFFE1F5FE))
            TeamMemberCircle("9", "9569786142", Color(0xFFEDE7F6))
            AddMemberCircle { navController.navigate("add_team_member") }
        }
    }
}

@Composable
fun TeamMemberCircle(initials: String, label: String, bgColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                initials,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            label,
            fontSize = 12.sp,
            color = Color.Gray,
            maxLines = 1
        )
    }
}

@Composable
fun AddMemberCircle(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFFE3F2FD)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color(0xFF1976D2))
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "Add\nMember",
            fontSize = 12.sp,
            color = Color.Gray,
            lineHeight = 14.sp,
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
   HomeScreen(navController = rememberNavController())
}
