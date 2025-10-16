package com.bitmax.digidial.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bitmax.digidial.screens.AddTeamMembersScreen
import com.bitmax.digidial.screens.AgentDashboardScreen
import com.bitmax.digidial.screens.CallRecordingsScreen
import com.bitmax.digidial.screens.CallScreenUI
import com.bitmax.digidial.screens.ClientProposalScreen
import com.bitmax.digidial.screens.ContactDetailsScreen
import com.bitmax.digidial.screens.EditProfileScreen
import com.bitmax.digidial.screens.GrantPermissionScreen
import com.bitmax.digidial.screens.HelpAndSupportScreen
import com.bitmax.digidial.screens.HomeScreen
import com.bitmax.digidial.screens.LoginScreen
import com.bitmax.digidial.screens.MeetingNotesScreen
import com.bitmax.digidial.screens.MyProfileScreen
import com.bitmax.digidial.screens.NewNoteScreen
import com.bitmax.digidial.screens.OTPVerificationScreen
import com.bitmax.digidial.screens.OwnerDashboardScreen
import com.bitmax.digidial.screens.RecordingScreen
import com.bitmax.digidial.screens.ReportsScreen
import com.bitmax.digidial.screens.SplashScreen
import com.bitmax.digidial.screens.SwitchAccountScreen
import com.bitmax.digidial.screens.ViewPlanScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.Splash.route
    ) {
        composable(Route.Splash.route) { SplashScreen(navController) }
        composable(
            route = Route.Login.route,
            arguments = listOf(navArgument("userType") { type = NavType.StringType })
        ) { backStackEntry ->
            val userType = backStackEntry.arguments?.getString("userType") ?: ""
            LoginScreen(navController = navController, userType = userType)
        }
        composable(Route.EditProfile.route) { EditProfileScreen(navController) }
        composable(Route.HomeScreen.route) { HomeScreen(navController) }
        composable(Route.Call.route) { CallScreenUI(navController) }
        composable(Route.CallRecording.route) { CallRecordingsScreen(navController) }
        composable(Route.Recording.route) { RecordingScreen(navController) }
        composable(Route.HelpAndSupport.route) { HelpAndSupportScreen(navController) }
        composable(Route.Dashboard.route) { OwnerDashboardScreen(navController) }
        composable(Route.MyProfile.route) { MyProfileScreen(navController) }
        composable(Route.NewNote.route) { NewNoteScreen(navController) }
        composable(Route.MeetingNotes.route) { MeetingNotesScreen(navController) }
        composable(Route.Reports.route) { ReportsScreen(navController) }
        composable(Route.ClientProposal.route) { ClientProposalScreen(navController) }
        composable(Route.SwitchAccount.route) { SwitchAccountScreen(navController = navController)}
        composable(Route.AgentDashboard.route) { AgentDashboardScreen(navController) }
        composable(Route.ViewPlan.route) { ViewPlanScreen(navController) }
        composable(Route.AddTeamMembers.route) { AddTeamMembersScreen(navController) }
        composable(
            route = Route.OtpVerification.route,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            OTPVerificationScreen(navController, phoneNumber)
        }
        composable(Route.GrantPermission.route) { GrantPermissionScreen(navController) }
        composable(Route.ContactDetails.route) { ContactDetailsScreen(navController) }
    }
}
