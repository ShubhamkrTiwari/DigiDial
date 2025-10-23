package com.bitmax.digidial.Navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bitmax.digidial.Screens.AddTeamMembersScreen
import com.bitmax.digidial.Screens.AgentDashboardScreen
import com.bitmax.digidial.Screens.CallRecordingsScreen
import com.bitmax.digidial.Screens.CallScreenUI
import com.bitmax.digidial.Screens.ClientProposalScreen
import com.bitmax.digidial.Screens.ContactDetailsScreen
import com.bitmax.digidial.Screens.EditProfileScreen
import com.bitmax.digidial.Screens.GrantPermissionScreen
import com.bitmax.digidial.Screens.HelpAndSupportScreen
import com.bitmax.digidial.Screens.HomeScreen
import com.bitmax.digidial.Screens.LoginScreen
import com.bitmax.digidial.Screens.MeetingNotesScreen
import com.bitmax.digidial.Screens.MyProfileScreen
import com.bitmax.digidial.Screens.NewNoteScreen
import com.bitmax.digidial.Screens.OTPVerificationScreen
import com.bitmax.digidial.Screens.OwnerDashboardScreen
import com.bitmax.digidial.Screens.RecordingScreen
import com.bitmax.digidial.Screens.ReportsScreen
import com.bitmax.digidial.Screens.SwitchAccountScreen
import com.bitmax.digidial.Screens.ViewPlanScreen
import com.bitmax.digidial.ViewModel.HomeScreenViewModel
import com.bitmax.digidial.screens.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val homeScreenViewModel: HomeScreenViewModel = viewModel()

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
            LoginScreen(navController = navController, userType = userType, viewModel = viewModel())
        }
        composable(Route.EditProfile.route) { EditProfileScreen(navController) }

        composable(Route.HomeScreen.route) { HomeScreen(navController, homeScreenViewModel) }
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
        composable(Route.SwitchAccount.route) { SwitchAccountScreen(navController = navController) }
        composable(Route.AgentDashboard.route) { AgentDashboardScreen(navController) }
        composable(Route.ViewPlan.route) { ViewPlanScreen(navController) }
        composable(Route.AddTeamMembers.route) { AddTeamMembersScreen(navController, homeScreenViewModel) }
        composable(
            route = Route.OtpVerification.route,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            OTPVerificationScreen(navController, phoneNumber, authViewModel = viewModel())
        }
        composable(Route.GrantPermission.route) { GrantPermissionScreen(navController) }
        composable(Route.ContactDetails.route) { ContactDetailsScreen(navController) }
    }
}