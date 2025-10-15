package com.bitmax.digidial.network

import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class AuthRepository(private val api: AuthApiService) {

    /**
     * Sends an OTP request.
     * @return The parsed [OtpResponse] on success.
     * @throws IOException on network failure or if the API returns an error.
     */
    suspend fun sendOtp(mobile: String): OtpResponse {
        val request = SendOtpRequest(mobile = mobile)
        try {
            val response = api.sendOtp(request)
            if (response.isSuccessful) {
                return response.body() ?: throw IOException("The server returned an empty response.")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    Gson().fromJson(errorBody, OtpResponse::class.java).message
                } catch (e: Exception) {
                    response.message()
                }
                throw IOException(errorMessage ?: "API error with no message.")
            }
        } catch (e: HttpException) {
            throw IOException("Network request failed: ${e.message()}", e)
        } catch (e: Exception) {
            // Rethrow non-HttpExceptions as IOExceptions to be handled by the ViewModel
            if (e is IOException) throw e
            throw IOException("An unexpected error occurred: ${e.localizedMessage}", e)
        }
    }

    /**
     * Sends an OTP verification request.
     * @return The parsed [VerifyOtpResponse] on success.
     * @throws IOException on network failure or if the API returns an error.
     */
    suspend fun verifyOtp(mobile: String, otp: String): VerifyOtpResponse {
        val request = VerifyOtpRequest(mobile = mobile, otp = otp)
        try {
            val response = api.verifyOtp(request)
            if (response.isSuccessful) {
                return response.body() ?: throw IOException("The server returned an empty response.")
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = try {
                    Gson().fromJson(errorBody, VerifyOtpResponse::class.java).message
                } catch (e: Exception) {
                    response.message()
                }
                throw IOException(errorMessage ?: "API error with no message.")
            }
        } catch (e: HttpException) {
            throw IOException("Network request failed: ${e.message()}", e)
        } catch (e: Exception) {
            if (e is IOException) throw e
            throw IOException("An unexpected error occurred: ${e.localizedMessage}", e)
        }
    }
}
