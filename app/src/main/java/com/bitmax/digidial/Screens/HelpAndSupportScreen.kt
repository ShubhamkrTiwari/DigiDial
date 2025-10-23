package com.bitmax.digidial.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bitmax.digidial.R


data class FAQ(val question: String, val answer: String, val status: String, val role: String)


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun HelpAndSupportScreen(navController: NavController) {
    var searchText by remember { mutableStateOf("") }

    val faqs = listOf(
        FAQ("What is Superfone and how is it different from a SIM card?",
            "Superfone is a cloud-based business phone system that works without a physical SIM card. It allows advanced features like IVR, CRM, and call recording.",
            "Active", "Support Agent"),
        FAQ("What Superfone plans are available?",
            "Superfone offers flexible monthly and yearly plans depending on features like call recording, IVR setup, CRM integration, and multiple users.",
            "Active", "Sales Lead"),
        FAQ("How can I select a Superfone number?",
            "You can choose a business number (vanity or regular) directly from the Superfone app during signup or later from settings.",
            "Active", "Support Agent"),
        FAQ("What is KYC verification in Superfone?",
            "KYC is a mandatory verification step to activate your Superfone number. You’ll need to upload valid ID proof as per TRAI guidelines.",
            "Active", "Support Agent"),
        FAQ("What are ringing patterns in Superfone?",
            "You can configure sequential ringing (calls ring one agent at a time) or parallel ringing (all agents at once).",
            "Active", "Support Agent"),
        FAQ("How do Roles & Permissions work in Superfone?",
            "Admins can assign roles (Owner, Manager, Agent) with specific permissions for calls, CRM, and number management.",
            "Active", "Admin"),
        FAQ("What is CRM integration in Superfone?",
            "Superfone integrates with Zoho, HubSpot, and Freshdesk to automatically log calls, notes, and customer details.",
            "Active", "Support Agent"),
        FAQ("How does Backup Number work in Superfone?",
            "You can set a backup mobile number so calls are forwarded if the app is offline.",
            "Active", "Support Agent"),
        FAQ("Can I set up IVR in Superfone?",
            "Yes, you can create IVR menus (Press 1 for Sales, 2 for Support) directly in the dashboard.",
            "Active", "Support Agent"),
        FAQ("Does Superfone support Caller Tunes?",
            "Yes, you can upload or select professional caller tunes that play while customers wait for your call.",
            "Active", "Support Agent"),
        FAQ("How does Call Recording work in Superfone?",
            "Superfone automatically records calls (if enabled in your plan) and stores them securely in your account.",
            "Active", "Support Agent")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Help & Support",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2) // Blue top bar
                )
            )
        },
        containerColor = Color(0xFFF5F8FF) // Background color
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {

            // Search Bar separated from FAQ list
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp)),
                placeholder = { Text("Search FAQs or guides") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Frequently Asked Questions",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E3A8A)
            )

            Spacer(modifier = Modifier.height(12.dp))

            val filteredFaqs = faqs.filter {
                it.question.contains(searchText, ignoreCase = true)
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredFaqs.size) { index ->
                    FAQItem(faq = filteredFaqs[index])
                }
            }
        }
    }
}

@Composable
fun FAQItem(faq: FAQ) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_faq),
                    contentDescription = null,
                    tint = Color(0xFF1E3A8A),
                    modifier = Modifier.size(28.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = faq.question,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        maxLines = if (expanded) Int.MAX_VALUE else 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = faq.role,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                StatusChip(faq.status)
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = faq.answer,
                    fontSize = 14.sp,
                    color = Color(0xFF4B5563),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun StatusChip(status: String) {
    val (bgColor, textColor) = if (status == "Active") {
        Color(0xFFD1FAE5) to Color(0xFF065F46)
    } else {
        Color(0xFFFECACA) to Color(0xFF7F1D1D)
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = status, fontSize = 12.sp, color = textColor, fontWeight = FontWeight.SemiBold)
    }
}
@Preview(showBackground = true)
@Composable
fun HelpAndSupportScreenPreview() {
    HelpAndSupportScreen(navController = rememberNavController())
}
