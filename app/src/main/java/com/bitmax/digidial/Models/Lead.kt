package com.bitmax.digidial.Models

import com.google.gson.annotations.SerializedName

data class Lead(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String,
    @SerializedName("last_call") val lastCall: String,
)
