package com.bitmax.digidial.Models

class CallRequest
    (
    private val team_id: Int,
    private val call_ssid: String?,
    private val from_number: String?,
    private val to_number: String?,
    private val status: String?
) {
    private val started_at: String? = null
    private val ended_at: String? = null
    private val recording_url: String? = null
}