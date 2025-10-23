package com.bitmax.digidial.Screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bitmax.digidial.R

@Preview(showBackground = true)
@Composable
fun SwitchAccountPreview() {
    SwitchAccountScreen(navController = rememberNavController())
}

@Composable
fun SwitchAccountScreen(
    navController: NavController
) {
    val lightBlue = Color(0xFFB3E5FC)
    val white = Color.White
    var selectedRole by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(white),
        color = white
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 🔷 TOP SECTION (Logo + Tagline)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.digidial_logo), // 🔹 Replace with your app logo
                    contentDescription = "Digidial Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(top = 16.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "DigiDial",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0288D1),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "Empowering Businesses" +
                            " with Smart Digital Solutions",
                    fontSize = 14.sp,
                    color = Color(0xFF0277BD),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 30.dp)
                )
            }

            // 🔹 MIDDLE SECTION (Title + Role Cards)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Switch Account",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0288D1),
                    modifier = Modifier.padding(bottom = 30.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RoleCard(
                        title = "Owner",
                        iconRes = R.drawable.ic_owner,
                        isSelected = selectedRole == "Owner",
                        onClick = {
                            selectedRole = "Owner"
                            navController.navigate("login/Owner")
                        },
                        lightBlue = lightBlue
                    )

                    RoleCard(
                        title = "Agent",
                        iconRes = R.drawable.ic_agent,
                        isSelected = selectedRole == "Agent",
                        onClick = {
                            selectedRole = "Agent"
                            navController.navigate("login/Agent")
                        },
                        lightBlue = lightBlue
                    )
                }
            }

            // 🔹 Powered by section
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Powered by",
                    fontSize = 14.sp,
                    color = Color.Gray,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.bitmaxtecnologylogo),
                    contentDescription = "Bitmax Logo",
                    modifier = Modifier.height(20.dp)
                )
            }
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    lightBlue: Color
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) lightBlue.copy(alpha = 0.4f) else Color.White,
        label = ""
    )

    Column(
        modifier = Modifier
            .width(150.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .background(backgroundColor, RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .background(lightBlue.copy(alpha = 0.3f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = title,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Fit
            )
        }

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF01579B),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}
