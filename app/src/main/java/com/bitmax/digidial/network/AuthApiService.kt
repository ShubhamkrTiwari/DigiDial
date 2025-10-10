package com.bitmax.digidial.network

import com.bitmax.digidial.Models.CallRequest
import com.bitmax.digidial.Models.CallResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("/api/digidial/auth/send-otp")
    suspend fun sendOtp(@Body request: ForgetPasswordRequest): Response<OtpResponse>

    @POST("/api/digidial/auth/verify")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

    @POST("/api/digidial/telephony/calls")
    fun createCall(@Body callRequest: CallRequest): Call<CallResponse>
}
