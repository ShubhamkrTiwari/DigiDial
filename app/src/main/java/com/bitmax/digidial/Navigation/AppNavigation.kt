package com.bitmax.digidial.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bitmax.digidial.Models.Customer
import com.bitmax.digidial.Screens.AddTeamMembersScreen
import com.bitmax.digidial.Screens.AgentDashboardScreen
import com.bitmax.digidial.Screens.CallRecordingsScreen
import com.bitmax.digidial.Screens.CallScreenUI
import com.bitmax.digidial.Screens.ClientProposalScreen
import com.bitmax.digidial.Screens.ContactDetailsScreen
import com.bitmax.digidial.Screens.CustomerListScreen
import com.bitmax.digidial.Screens.EditProfileScreen
import com.bitmax.digidial.Screens.GrantPermissionScreen
import com.bitmax.digidial.Screens.HelpAndSupportScreen
import com.bitmax.digidial.Screens.HomeScreen
import com.bitmax.digidial.Screens.Login
import com.bitmax.digidial.Screens.MeetingNotesScreen
import com.bitmax.digidial.Screens.MyProfileScreen
import com.bitmax.digidial.Screens.NewNoteScreen
import com.bitmax.digidial.Screens.OTPVerificationScreen
import com.bitmax.digidial.Screens.OutgoingCallScreen
import com.bitmax.digidial.Screens.RecordingScreen
import com.bitmax.digidial.Screens.ReportsScreen
import com.bitmax.digidial.Screens.SplashScreen
import com.bitmax.digidial.Screens.OwnerDashboardScreen
import com.bitmax.digidial.Screens.SwitchAccountScreen
import com.bitmax.digidial.Screens.ViewPlanScreen
import com.google.gson.Gson
import java.net.URLDecoder


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "Splash"
    ) {
        composable("Splash") { SplashScreen(navController) }
        composable("login") { Login(navController) }
        composable("editprofile") { EditProfileScreen(navController) }
        composable(
            route = "homeScreen/{phoneNumber}",
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            HomeScreen(navController, phoneNumber)
        }
        composable("customerlist") { CustomerListScreen(navController) }
        composable("call") { CallScreenUI(navController) }
        composable("callrecording") { CallRecordingsScreen(navController) }
        composable("recording") { RecordingScreen(navController) }
        composable("helpandsupport") { HelpAndSupportScreen(navController) }
        composable("dashboard") { OwnerDashboardScreen(navController) }
        composable("myprofile") { MyProfileScreen(navController) }
        composable("new_note") { NewNoteScreen(navController) }
        composable("meeting_notes") { MeetingNotesScreen(navController) }
        composable("reports") { ReportsScreen(navController) }
        composable("client_proposal") { ClientProposalScreen(navController) }
        composable("switch_account") { SwitchAccountScreen(navController)}
        composable("agentdashboard") { AgentDashboardScreen(navController) }
        composable("viewplan") { ViewPlanScreen(navController) }



        //composable("notification"){ NotificationScreen(navController) }
        composable("addteammembers") { AddTeamMembersScreen(navController) }
        composable(
            route = "otpverification/{phoneNumber}",
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            OTPVerificationScreen(navController, phoneNumber)
        }


        composable("grantpermission") { GrantPermissionScreen(navController) }
//        composable("calldetails") { CallDetailsScreen(navController) }
//        composable("incomingcall") { IncomingScreen (navController) }
        composable(
            route = "outgoingCall/{customerJson}",
            arguments = listOf(
                navArgument("customerJson") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString("customerJson") ?: ""
            val customer = Gson().fromJson(json, Customer::class.java)
            OutgoingCallScreen(customer = customer)
        }
        // 🔹 Customer list screen
        composable("customerList") {
            CustomerListScreen(navController)
        }

        // 🔹 Contact details screen with full Customer object
        composable("contactDetails/{customerJson}") { backStackEntry ->
            val json = backStackEntry.arguments?.getString("customerJson")
            val decoded = URLDecoder.decode(json ?: "", "UTF-8")
            val customer = Gson().fromJson(decoded, Customer::class.java)

            ContactDetailsScreen(customer = customer, navController)
        }

    }
}