package com.bitmax.digidial.Models

import androidx.annotation.DrawableRes

data class Customer(
    val id: Int,
    val name: String,
    val company: String? = null,
    @DrawableRes val profileImage: Int? = null,
    val currentCallDuration: String? = null,
    val phone: String, // Added phone field
    val lastCall: String // Added lastCall field
)
