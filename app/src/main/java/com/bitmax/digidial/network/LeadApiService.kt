package com.bitmax.digidial.network

import com.bitmax.digidial.Models.Lead
import retrofit2.http.GET

interface LeadApiService {
    @GET("leads")
    suspend fun getLeads(): List<Lead>
}
