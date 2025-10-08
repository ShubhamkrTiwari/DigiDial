package com.bitmax.digidial.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bitmax.digidial.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyDetailsScreen(navController: NavController) {

    var fullName by rememberSaveable { mutableStateOf("") }
    var businessName by rememberSaveable { mutableStateOf("") }
    var businessEmail by rememberSaveable { mutableStateOf("") }
    var businessType by rememberSaveable { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    val businessTypes = listOf("Retail", "Wholesale", "Service", "Manufacturing", "Other")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 🔹 Top Banner with Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF1976D2), Color(0xFF42A5F5))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.digidial_logo),
                    contentDescription = "DigiDial Logo",
                    modifier = Modifier.height(64.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Smart Business Solutions",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🔹 Page Title
        Text(
            text = "Company Details",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Enter your details to get started",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(28.dp))

        // 🔹 Modern Input Fields
        ModernCardTextField(value = fullName, onValueChange = { fullName = it }, label = "Full Name")
        Spacer(modifier = Modifier.height(16.dp))
        ModernCardTextField(value = businessName, onValueChange = { businessName = it }, label = "Business Name")
        Spacer(modifier = Modifier.height(16.dp))
        ModernCardTextField(value = businessEmail, onValueChange = { businessEmail = it }, label = "Business Email", keyboardType = KeyboardType.Email)
        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Dropdown for Business Type
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                    .clickable { expanded = !expanded }
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (businessType.isEmpty()) "Select Business Type" else businessType,
                        color = if (businessType.isEmpty()) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                businessTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            businessType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 🔹 Continue Button
        Button(
            onClick = { navController.navigate("addteammembers") },
            enabled = fullName.isNotBlank() && businessName.isNotBlank() && businessEmail.isNotBlank() && businessType.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(56.dp)
                .shadow(6.dp, RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Continue", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.weight(1f))

        // 🔹 Footer
        Text(
            text = "Powered by Bitmax",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(bottom = 50.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}


@Composable
fun ModernCardTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp) // ⬅️ dono ends se gap
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(text = label, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                innerTextField()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
@Preview(showBackground = true)
@Composable
fun CompanyDetailsScreenPreview() {
    CompanyDetailsScreen(navController = rememberNavController())
}
