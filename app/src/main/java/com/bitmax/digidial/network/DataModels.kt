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



