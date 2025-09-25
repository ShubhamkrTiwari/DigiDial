package com.bitmax.digidial.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bitmax.digidial.Screens.CallRecordingsScreen
import com.bitmax.digidial.Screens.CallScreenUI
import com.bitmax.digidial.Screens.CustomerListScreen
import com.bitmax.digidial.Screens.DashboardScreen
import com.bitmax.digidial.Screens.EditProfileScreen
import com.bitmax.digidial.Screens.HelpAndSupportScreen
import com.bitmax.digidial.Screens.HomeScreen
import com.bitmax.digidial.Screens.LoginScreen
import com.bitmax.digidial.Screens.MyProfileScreen
import com.bitmax.digidial.Screens.NotificationScreen
import com.bitmax.digidial.Screens.RecordingScreen
import com.bitmax.digidial.Screens.SignUpScreen
import com.bitmax.digidial.Screens.SplashScreen


@Composable
fun AppNavigation() {
    val navController=rememberNavController()
    NavHost(
        navController =navController,
        startDestination = "Splash"
    ){
        composable("Splash"){ SplashScreen(navController) }
        composable("login"){ LoginScreen(navController) }
        composable("signup"){ SignUpScreen(navController) }
        composable("editprofile"){ EditProfileScreen(navController) }
        composable("homeScreen"){ HomeScreen(navController) }
        composable("customerlist"){ CustomerListScreen(navController) }
        composable("call"){ CallScreenUI(navController) }
        composable("callrecording"){ CallRecordingsScreen(navController) }
        composable("recording"){ RecordingScreen(navController) }
        composable("helpandsupport"){ HelpAndSupportScreen(navController) }
        composable("dashboard"){ DashboardScreen(navController) }
        composable("myprofile"){ MyProfileScreen(navController) }
        //composable("notification"){ NotificationScreen(navController) }




    }
}