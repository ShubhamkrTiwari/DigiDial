package com.bitmax.digidial.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bitmax.digidial.R

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GrantPermissionScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Error Image
        Image(
            painter = painterResource(id = R.drawable.error_phone), // your sad phone drawable
            contentDescription = "Error Phone",
            modifier = Modifier
                .size(140.dp)
                .padding(top = 20.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Title
        Text(
            text = "There seems to be a problem in running the app",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Description
        Text(
            text = "To use Superfone, we require the following permissions.",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Permission List
        PermissionItem(
            icon = R.drawable.ic_mic,
            title = "Access to microphone & SPEAKER",
            desc = "To make calls, we need access to your microphone and speaker"
        )
        Spacer(modifier = Modifier.height(20.dp))

        PermissionItem(
            icon = R.drawable.ic_battery,
            title = "Battery optimization",
            desc = "Needed to avoid missing calls or losing the connection to Superfone"
        )
        Spacer(modifier = Modifier.height(20.dp))

        PermissionItem(
            icon = R.drawable.ic_overlay,
            title = "Draw over other apps",
            desc = "Superfone can notify you of incoming calls even when other apps are being used"
        )
        Spacer(modifier = Modifier.height(20.dp))

        PermissionItem(
            icon = R.drawable.ic_notifications,
            title = "Notification Permission",
            desc = "Allows app to show notification for incoming calls, missed calls and reminders"
        )
        Spacer(modifier = Modifier.height(20.dp))

        PermissionItem(
            icon = R.drawable.ic_phone,
            title = "Phone Permission",
            desc = "Allows app to manage incoming calls, missed calls and outgoing calls"
        )
        Spacer(modifier = Modifier.height(20.dp))

        PermissionItem(
            icon = R.drawable.ic_phone_call,
            title = "Phone Call Permission",
            desc = "Allows app to do outgoing calls"
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Security Info
        Text(
            text = "We follow high security standards as per regulations. All your data is safe and secure.",
            fontSize = 13.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Grant Button
        Button(
            onClick = { /* TODO: request permissions */ },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "GRANT PERMISSIONS",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(6.dp)
            )
        }
    }
}

@Composable
fun PermissionItem(icon: Int, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp)
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = desc,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }

        Image(
            painter = painterResource(id = R.drawable.warning), // red exclamation icon
            contentDescription = "Warning",
            modifier = Modifier.size(20.dp)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun GrantPermissionScreenPreview() {
    GrantPermissionScreen(navController = rememberNavController())
}
