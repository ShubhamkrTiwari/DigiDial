package com.bitmax.digidial.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitmax.digidial.Models.Lead
import com.bitmax.digidial.network.ApiClient
import com.bitmax.digidial.network.LeadApiService
import kotlinx.coroutines.launch

class LeadViewModel : ViewModel() {
    val leads = mutableStateOf<List<Lead>>(emptyList())
    val errorMessage = mutableStateOf("")

    private val leadApi: LeadApiService = ApiClient.leadApi

    fun fetchLeads() {
        viewModelScope.launch {
            try {
                leads.value = leadApi.getLeads()
            } catch (e: Exception) {
                errorMessage.value = "Failed to fetch leads: ${e.message}"
            }
        }
    }
}
