package com.bitmax.digidial.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bitmax.digidial.navigation.Route
import com.bitmax.digidial.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val scrollState = rememberScrollState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFD8EAFE), Color.White)
                    )
                )
            ) {
                Spacer(Modifier.height(24.dp))
                Text(
                    "Switch Role",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RoleAvatar(
                        icon = Icons.Outlined.Business,
                        label = "Owner",
                        isSelected = currentRoute == Route.Dashboard.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Route.Dashboard.route)
                        }
                    )

                    RoleAvatar(
                        icon = Icons.Outlined.Person,
                        label = "Agent",
                        isSelected = currentRoute == Route.AgentDashboard.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(Route.AgentDashboard.route)
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFE8F5FF), Color.White)
                        )
                    )
                    .verticalScroll(scrollState)
            ) {
                // Pass a lambda to open the drawer
                TrialAccountSection(
                    navController = navController,
                    authViewModel = authViewModel,
                    onMenuClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
                MyTeamSection(navController)

                Spacer(modifier = Modifier.height(16.dp))

                // 🔹 Quick Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QuickActionCard(Icons.Default.Call, "Quick Call") { navController.navigate(Route.Call.route) }
                    QuickActionCard(Icons.Default.NoteAdd, "New Note") { navController.navigate(Route.NewNote.route) }
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
                    FileCard("Reports", Icons.Default.PictureAsPdf) { navController.navigate(Route.Reports.route) }
                    FileCard("Meeting Notes", Icons.Default.Description) { navController.navigate(Route.MeetingNotes.route) }
                    FileCard("Client Proposal", Icons.Default.InsertDriveFile) { navController.navigate(Route.ClientProposal.route) }
                }

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
}

@Composable
fun RoleAvatar(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    val selectedColor = MaterialTheme.colorScheme.primary
    val unselectedColor = Color.Gray

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(if (isSelected) selectedColor.copy(alpha = 0.15f) else Color.Black.copy(alpha = 0.05f))
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) selectedColor else Color.Gray.copy(alpha = 0.3f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(40.dp),
                tint = if (isSelected) selectedColor else unselectedColor
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = label,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) selectedColor else unselectedColor,
            fontSize = 14.sp
        )
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileCard(name: String, icon: ImageVector, onClick: (() -> Unit)? = null) {
    Card(
        onClick = { onClick?.invoke() },
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
            progress = { progress },
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
                Icon(icon, title)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold)
                subtitle?.let { Text(it, color = Color.Gray, fontSize = 12.sp) }
            }
            endIcon?.let {
                Icon(it, contentDescription = null)
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFE3F2FD), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(name.first().toString(), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(0xFF1976D2))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold)
                Text(company, color = Color.Gray, fontSize = 12.sp)
            }
            IconButton(onClick = onCall) {
                Icon(Icons.Default.Call, contentDescription = "Call")
            }
        }
    }
}

@Composable
fun TrialAccountSection(navController: NavController, authViewModel: AuthViewModel, onMenuClick: () -> Unit) {
    val phoneNumber by authViewModel.phoneNumber.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF1A73E8), Color(0xFF1976D2))
                )
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
            }
            Text("Trial Account", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            IconButton(onClick = { navController.navigate(Route.MyProfile.route) }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = Color.White, modifier = Modifier.size(32.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(phoneNumber, color = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

@Composable
fun MyTeamSection(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("My Team", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            TextButton(onClick = { navController.navigate(Route.AddTeamMembers.route) }) {
                Text("View All")
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            // Add team member avatars here
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    BottomAppBar {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            IconButton(onClick = { navController.navigate(Route.Dashboard.route) }) {
                Icon(Icons.Default.Home, contentDescription = "Home")
            }
            IconButton(onClick = { navController.navigate(Route.Reports.route) }) {
                Icon(Icons.Default.Assessment, contentDescription = "Reports")
            }
            IconButton(onClick = { /* Navigate to contacts */ }) {
                Icon(Icons.Default.People, contentDescription = "Contacts")
            }
            IconButton(onClick = { navController.navigate(Route.MyProfile.route) }) {
                Icon(Icons.Default.Person, contentDescription = "Profile")
            }
        }
    }
}

@Composable
fun AppFooter() {
    Text(
        text = "DigiDial Inc.",
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        textAlign = TextAlign.Center,
        color = Color.Gray
    )
}
