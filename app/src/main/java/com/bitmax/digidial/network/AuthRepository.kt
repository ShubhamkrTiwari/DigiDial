package com.bitmax.digidial.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

suspend fun <T : Any> safeApiCall(apiCall: suspend () -> T): NetworkResult<T> {
    return withContext(Dispatchers.IO) {
        try {
            NetworkResult.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    NetworkResult.Error(throwable.code(), throwable.response()?.errorBody()?.string())
                }
                else -> {
                    NetworkResult.Exception(throwable)
                }
            }
        }
    }
}

class AuthRepository(private val api: AuthApiService) {

    suspend fun sendOtp(mobile: String): NetworkResult<OtpResponse> {
        return safeApiCall { api.sendOtp(SendOtpRequest(mobile = mobile)) }
    }

    suspend fun verifyOtp(mobile: String, otp_code: String): NetworkResult<VerifyOtpResponse> {
        return safeApiCall { api.verifyOtp(VerifyOtpRequest(mobile = mobile, otp_code = otp_code)) }
    }
}
