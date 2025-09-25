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
    object DashboardScreen :Route("dashboard")
    object MyProfile :Route("myprofile")
    object Notification :Route("notification")


}