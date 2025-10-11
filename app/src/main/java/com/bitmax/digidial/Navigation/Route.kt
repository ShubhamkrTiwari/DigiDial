package com.bitmax.digidial.Navigation

sealed class Route(val route: String) {
    object Splash :Route("Splash")
    object Login :Route("login")
    object Signup :Route("signup")
    object EditProfile:Route("editprofile")
    object HomeScreen :Route("homeScreen")
    object CustomerList :Route("customerlist")
    object Call :Route("call")
    object CallRecording :Route("callrecording")
    object Recording :Route("recording")
    object HelpAndSupport :Route("helpandsupport")
    object OwnerDashboardScreen :Route(route = "dashboard")
    object MyProfile :Route("myprofile")
    object Notification :Route("notification")
    object  AddTeamMembers: Route("addteammembers")
    object OTPVerification : Route("otpverification/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "otpverification/$phoneNumber"
    }
    object GrantPermission: Route("grantpermission")
    object CompanyDetails : Route("companydetails")
    object ContactDetailsScreen : Route("contactdetails")
    object CallDetailsScreen : Route("calldetails")
    object IncomingScreen : Route("incomingcall")
    object OutgoingCallScreen : Route("outgoingcall")
    object SwitchAccountScreen : Route("switch_account")
    object AgentDashboardScreen : Route("agentdashboard")
    object ViewPlanScreen : Route("viewplan")


}