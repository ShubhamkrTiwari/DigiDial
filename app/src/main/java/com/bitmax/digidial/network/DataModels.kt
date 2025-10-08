package com.bitmax.digidial.network

import com.google.gson.annotations.SerializedName

data class ForgetPasswordRequest(
    @SerializedName("mobile")
    val mobile: String
)

data class OtpResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String
)

data class VerifyOtpRequest(
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("otp_code")
    val otp: String
)



data class UserData(
    @SerializedName("token")
    val token: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("mobile")
    val mobile: String,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("isVerified")
    val isVerified: Boolean
)
