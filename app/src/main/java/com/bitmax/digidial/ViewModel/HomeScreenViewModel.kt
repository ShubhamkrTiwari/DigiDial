package com.bitmax.digidial.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitmax.digidial.Models.Customer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class TeamMember(
    val initials: String,
    val name: String,
    val phoneNumber: String,
    val color: androidx.compose.ui.graphics.Color
)

class HomeScreenViewModel : ViewModel() {

    private val _teamMembers = MutableStateFlow<List<TeamMember>>(emptyList())
    val teamMembers: StateFlow<List<TeamMember>> = _teamMembers

    init {
        loadTeamMembers()
    }

    private fun loadTeamMembers() {
        viewModelScope.launch {
            // Replace with your actual data fetching logic
            _teamMembers.value = emptyList()
        }
    }

    fun addTeamMember(name: String, phoneNumber: String) {
        viewModelScope.launch {
            val initials = name.take(2).uppercase()
            val newMember = TeamMember(initials, name, phoneNumber, getRandomColor())
            _teamMembers.value = _teamMembers.value + newMember
        }
    }

    private fun getRandomColor(): androidx.compose.ui.graphics.Color {
        val colors = listOf(
            androidx.compose.ui.graphics.Color(0xFFFFCDD2),
            androidx.compose.ui.graphics.Color(0xFFE1F5FE),
            androidx.compose.ui.graphics.Color(0xFFEDE7F6),
            androidx.compose.ui.graphics.Color(0xFFFCE4EC),
            androidx.compose.ui.graphics.Color(0xFFE8EAF6),
        )
        return colors.random()
    }
}