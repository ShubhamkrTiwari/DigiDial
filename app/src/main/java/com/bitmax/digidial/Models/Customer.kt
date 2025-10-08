package com.bitmax.digidial.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Customer(
    val id: Int,
    val name: String,
    val company: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val profileImage: Int? = null, // drawable resource id
    val currentCallDuration: String? = null,
    val callHistory: List<String> = emptyList(),

    // 🔹 New fields
    val type: String = "All",      // "All", "Lead", "VIP"
    val lastCall: String = "N/A",  // last call info
    val isNew: Boolean = false     // mark as new customer
) : Parcelable
