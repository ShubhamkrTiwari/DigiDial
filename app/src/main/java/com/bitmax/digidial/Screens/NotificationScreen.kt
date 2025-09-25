package com.bitmax.digidial.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// 1) Data class
data class NotificationItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val subtitle: String,
    val section: String,
    val trailingIcon: (@Composable (() -> Unit))? = null,
    val onClick: (() -> Unit)? = null
)

// 2) NotificationCard - ek ek card ka UI
@Composable
fun NotificationCard(notification: NotificationItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f)) {
                Icon(
                    imageVector = notification.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = notification.title, style = MaterialTheme.typography.titleMedium)
                    Text(text = notification.subtitle, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
            notification.trailingIcon?.invoke()
        }
    }
}

// 3) Screen with Header + List
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(notifications: List<NotificationItem>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Blue header
        TopAppBar(
            title = { Text("Notifications", color = Color.White) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF1976D2) // Blue
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Group notifications by section
            notifications.groupBy { it.section }.forEach { (section, items) ->
                item {
                    Text(
                        text = section,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                items(items) { notif ->
                    NotificationCard(notification = notif)
                }
            }
        }
    }
}

// 4) Preview
@Preview(showBackground = true)
@Composable
fun PreviewNotificationScreen() {
    val notifications = listOf(
        NotificationItem(
            icon = Icons.Default.Notifications,
            title = "Reminder: Follow up with",
            subtitle = "3 days ago",
            section = "Reminders",
            trailingIcon = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
        ),
        NotificationItem(
            icon = Icons.Default.Notifications,
            title = "New Call Recording: Johnson",
            subtitle = "20 May, 9:20 AM",
            section = "Today",
            trailingIcon = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }
        ),
        NotificationItem(
            icon = Icons.Default.Mic,
            title = "New Call Recording: John Smith (1:30)",
            subtitle = "John Smith (1:30)",
            section = "Today"
        ),
        NotificationItem(
            icon = Icons.Default.CalendarMonth,
            title = "Meeting Scheduled: Sarah Chen",
            subtitle = "Tomorrow at 10 AM",
            section = "Today"
        ),
        NotificationItem(
            icon = Icons.Default.Notifications,
            title = "Some other notification",
            subtitle = "Yesterday at 8 PM",
            section = "Yesterday"
        )
    )

    NotificationScreen(notifications)
}
