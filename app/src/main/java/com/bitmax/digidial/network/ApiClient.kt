package com.bitmax.digidial.network

//import android.content.Context
//import okhttp3.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class ApiClient(context: Context) {
//
//    private val loggingInterceptor = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }
//
//    private val authInterceptor = AuthInterceptor(context)
//
//    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor(loggingInterceptor)
//        .addInterceptor(authInterceptor) // Add the AuthInterceptor
//        .build()
//
//    private val retrofit: Retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    val authApi: AuthApiService = retrofit.create(AuthApiService::class.java)
//
//
//    companion object {
//        private const val BASE_URL = "https://supernova-5aag.onrender.com/"
//
//        @Volatile
//        private var INSTANCE: ApiClient? = null
//
//        fun getInstance(): ApiClient {
//            return INSTANCE ?: synchronized(this) {
//                val instance = ApiClient(App.appContext)
//                INSTANCE = instance
//                instance
//            }
//        }
//
//        val authApi: AuthApiService by lazy { getInstance().authApi }
//
//    }
//}
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "https://supernova-5aag.onrender.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApiService = retrofit.create(AuthApiService::class.java)
}

