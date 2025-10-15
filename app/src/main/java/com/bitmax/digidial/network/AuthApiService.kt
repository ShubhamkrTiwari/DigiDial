package com.bitmax.digidial.network

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/digidial/auth/send-otp")
    suspend fun sendOtp(@Body request: SendOtpRequest): OtpResponse

    @POST("api/digidial/auth/verify")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): VerifyOtpResponse
}
