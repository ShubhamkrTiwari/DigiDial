package com.bitmax.digidial.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bitmax.digidial.Navigation.Route
import com.bitmax.digidial.ViewModel.HomeScreenViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = viewModel()
) {
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
            TrialAccountSection(navController)
            MyTeamSection(navController, homeScreenViewModel)

            Spacer(modifier = Modifier.height(16.dp))

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
                FileCard("Q3 Report.pdf", Icons.Default.PictureAsPdf) { navController.navigate(Route.Reports.route) }
                FileCard("Meeting_Notes.docx", Icons.Default.Description) { navController.navigate(Route.MeetingNotes.route) }
                FileCard("Client_Proposal", Icons.Default.InsertDriveFile) { navController.navigate(Route.ClientProposal.route) }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text("DAILY DIGEST", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(16.dp, 20.dp, 16.dp, 8.dp))

            DigestCard(Icons.Default.BarChart, "Activity: 125% of goal")
            DigestCard(Icons.Default.Star, "Top Contact: Sarah Chen", "(5 interactions)", Icons.Default.Cloud)
            DigestCard(Icons.Default.Star, "Top Contact: Sarh 3h 30m", "(interactions)", Icons.Default.AccessTime)
        }
    }
}

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileCard(name: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
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
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        containerColor = Color.White
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentRoute == Route.HomeScreen.route,
            onClick = { navController.navigate(Route.HomeScreen.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Contacts, contentDescription = "Contacts") },
            label = { Text("Contacts") },
            selected = currentRoute == Route.ContactDetails.route,
            onClick = { navController.navigate(Route.ContactDetails.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Call, contentDescription = "Calls") },
            label = { Text("Calls") },
            selected = currentRoute == Route.Call.route,
            onClick = { navController.navigate(Route.Call.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = "CallRecordings") },
            label = { Text("Recording") },
            selected = currentRoute == Route.CallRecording.route,
            onClick = { navController.navigate(Route.CallRecording.route) }
        )
    }
}

@Composable
fun TrialAccountSection(navController: NavController) {
    var menuExpanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2196F3))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { menuExpanded = true }
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
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Owner") },
                        onClick = {
                            navController.navigate(Route.Dashboard.route)
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Agent") },
                        onClick = {
                            navController.navigate(Route.AgentDashboard.route)
                            menuExpanded = false
                        }
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.Help,
                contentDescription = "Help",
                tint = Color(0xFFFF9800),
                modifier = Modifier
                    .size(26.dp)
                    .clickable { navController.navigate(Route.HelpAndSupport.route) }
            )
        }

        Spacer(Modifier.height(10.dp))

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
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { navController.navigate(Route.ViewPlan.route) }
                )
            }
        }
    }
}

@Composable
fun MyTeamSection(navController: NavController, viewModel: HomeScreenViewModel) {
    val teamMembers by viewModel.teamMembers.collectAsState()

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
            teamMembers.forEach {
                TeamMemberCircle(it.initials, it.name, it.color)
            }
            AddMemberCircle { navController.navigate(Route.AddTeamMembers.route) }
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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
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
        Text("Add Member", fontSize = 12.sp, color = Color.Gray, lineHeight = 14.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}
