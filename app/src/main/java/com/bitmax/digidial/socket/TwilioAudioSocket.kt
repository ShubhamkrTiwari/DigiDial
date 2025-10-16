//package com.apoorv.myvoice.socket
//
//import android.util.Base64
//import android.util.Log
//import okhttp3.*
//import okio.ByteString
//import org.json.JSONObject
//import java.util.concurrent.TimeUnit
//
//class TwilioAudioSocket(private val wsUrl: String) {
//
//    private val TAG = "CallActivity"
//    private var webSocket: WebSocket? = null
//    private val client: OkHttpClient = OkHttpClient.Builder()
//        .pingInterval(30, TimeUnit.SECONDS)
//        .build()
//
//    private val audioPlayer = AudioPlayer(16000) // match sender's sample rate
//
//    fun connect() {
//        val request = Request.Builder().url(wsUrl).build()
//        webSocket = client.newWebSocket(request, object : WebSocketListener() {
//
//            override fun onOpen(ws: WebSocket, response: Response) {
//                Log.d(TAG, "✅ WebSocket connected")
//            }
//
//            // Incoming text messages (Twilio Live Audio)
//            override fun onMessage(ws: WebSocket, text: String) {
//                try {
//                    val json = JSONObject(text)
//                    val payload = json.getJSONObject("media").getString("payload")
//                    val pcmBytes = Base64.decode(payload, Base64.NO_WRAP)
//
//                    audioPlayer.playAudio(pcmBytes)
//                } catch (e: Exception) {
//                    Log.e(TAG, "❌ Error decoding incoming audio: ${e.message}")
//                }
//            }
//
//            // If any raw binary comes (should not in Twilio Live Audio)
//            override fun onMessage(ws: WebSocket, bytes: ByteString) {
//                val pcmData = bytes.toByteArray()
//                audioPlayer.playAudio(pcmData)
//                Log.d(TAG, "🔊 Played ${pcmData.size} bytes of incoming binary audio")
//            }
//
//            override fun onClosing(ws: WebSocket, code: Int, reason: String) {
//                Log.d(TAG, "⚠️ WebSocket closing: $code / $reason")
//                ws.close(code, reason)
//            }
//
//            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
//                Log.d(TAG, "🛑 WebSocket closed: $code / $reason")
//            }
//
//            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
//                Log.e(TAG, "❌ WebSocket failure: ${t.message}", t)
//            }
//        })
//    }
//
//    fun isConnected(): Boolean = webSocket != null
//
//    fun send(message: String) {
//        if (isConnected()) {
//            val sent = webSocket?.send(message) ?: false
//            if (!sent) Log.w(TAG, "⚠️ Failed to send message")
//        } else {
//            Log.w(TAG, "⚠️ WebSocket not connected, cannot send message")
//        }
//    }
//
//    fun disconnect() {
//        try {
//            audioPlayer.stop()
//            webSocket?.close(1000, "Normal closure")
//            webSocket = null
//            Log.d(TAG, "🛑 WebSocket disconnected")
//        } catch (e: Exception) {
//            Log.e(TAG, "❌ Error while disconnecting WebSocket", e)
//        }
//    }
//}

package com.apoorv.myvoice.socket

import android.util.Base64
import android.util.Log
import okhttp3.*
import okio.ByteString
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class TwilioAudioSocket(private val wsUrl: String) {

    private val TAG = "TwilioAudioSocket"
    private var webSocket: WebSocket? = null
    private val client: OkHttpClient = OkHttpClient.Builder()
        .pingInterval(30, TimeUnit.SECONDS)
        .build()

    private val audioPlayer = AudioPlayer(8000) // Twilio phone quality = 8kHz µ-law

    fun connect() {
        val request = Request.Builder().url(wsUrl).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(ws: WebSocket, response: Response) {
                Log.d(TAG, "✅ WebSocket connected to $wsUrl")
            }

            // Incoming TEXT messages (JSON format)
            override fun onMessage(ws: WebSocket, text: String) {
                try {
                    Log.d(TAG, "📨 Received TEXT message: ${text.take(100)}...")

                    val json = JSONObject(text)

                    // Check if it's a media event
                    if (json.has("event") && json.getString("event") == "media") {
                        if (json.has("media")) {
                            val mediaObj = json.getJSONObject("media")
                            if (mediaObj.has("payload")) {
                                val payload = mediaObj.getString("payload")
                                val pcmBytes = Base64.decode(payload, Base64.NO_WRAP)

                                audioPlayer.playAudio(pcmBytes)
                                Log.d(TAG, "🔊 Decoded ${pcmBytes.size} bytes from base64")
                            } else {
                                Log.w(TAG, "⚠️ 'payload' not found in media object")
                            }
                        } else {
                            Log.w(TAG, "⚠️ 'media' object not found in JSON")
                        }
                    } else {
                        Log.d(TAG, "ℹ️ Non-media event: ${json.optString("event", "unknown")}")
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "❌ Error processing TEXT message: ${e.message}", e)
                    // Fallback: try to decode as raw base64
                    try {
                        val pcmBytes = Base64.decode(text, Base64.NO_WRAP)
                        audioPlayer.playAudio(pcmBytes)
                        Log.d(TAG, "🔊 Decoded ${pcmBytes.size} bytes (fallback)")
                    } catch (e2: Exception) {
                        Log.e(TAG, "❌ Fallback decoding also failed")
                    }
                }
            }

            // Incoming BINARY messages (raw audio)
            override fun onMessage(ws: WebSocket, bytes: ByteString) {
                val pcmData = bytes.toByteArray()
                // Log every 50th packet only to reduce spam
                if (System.currentTimeMillis() % 50 == 0L) {
                    Log.d(TAG, "📨 Receiving audio: ${pcmData.size} bytes")
                }
                audioPlayer.playAudio(pcmData)
            }

            override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "⚠️ WebSocket closing: $code / $reason")
                ws.close(code, reason)
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "🛑 WebSocket closed: $code / $reason")
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "❌ WebSocket failure: ${t.message}", t)
            }
        })
    }

    fun isConnected(): Boolean = webSocket != null

    fun send(message: String) {
        if (isConnected()) {
            val sent = webSocket?.send(message) ?: false
            if (!sent) {
                Log.w(TAG, "⚠️ Failed to send message")
            }
        } else {
            Log.w(TAG, "⚠️ WebSocket not connected, cannot send")
        }
    }

    fun disconnect() {
        try {
            audioPlayer.stop()
            webSocket?.close(1000, "Normal closure")
            webSocket = null
            Log.d(TAG, "🛑 WebSocket disconnected")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error disconnecting", e)
        }
    }
}