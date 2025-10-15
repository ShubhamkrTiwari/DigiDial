package com.bitmax.digidial.network

import java.io.IOException

class AuthRepository(private val api: AuthApiService) {

    /**
     * Sends an OTP request.
     * @return The parsed [OtpResponse] on success.
     * @throws IOException on network failure or if the API returns an error.
     */
    suspend fun sendOtp(mobile: String): OtpResponse {
        val request = SendOtpRequest(mobile = mobile)
        return api.sendOtp(request)
    }

    /**
     * Sends an OTP verification request.
     * @return The parsed [VerifyOtpResponse] on success.
     * @throws IOException on network failure or if the API returns an error.
     */
    suspend fun verifyOtp(mobile: String, otp_code: String): VerifyOtpResponse {
        val request = VerifyOtpRequest(mobile = mobile, otp_code = otp_code)
        return api.verifyOtp(request)
    }
}
