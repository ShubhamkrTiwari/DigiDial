package com.apoorv.myvoice.socket

import android.Manifest
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MicStreamer(private val socket: TwilioAudioSocket) {

    private val TAG = "MicStreamer"
    private val sampleRate = 8000 // Changed to 8kHz to match Twilio
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )
    private var recorder: AudioRecord? = null
    private var job: Job? = null
    private var packetCount = 0

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    fun startStreaming() {
        recorder = AudioRecord(
            MediaRecorder.AudioSource.VOICE_COMMUNICATION, // Better for calls
            sampleRate,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize * 4
        )

        if (recorder?.state != AudioRecord.STATE_INITIALIZED) {
            Log.e(TAG, "❌ AudioRecord initialization failed")
            return
        }

        recorder?.startRecording()
        Log.d(TAG, "🎤 Started recording at $sampleRate Hz")

        job = CoroutineScope(Dispatchers.IO).launch {
            val pcmBuffer = ByteArray(320) // 160 samples × 2 bytes = 320 bytes

            while (isActive) {
                val read = recorder?.read(pcmBuffer, 0, pcmBuffer.size) ?: 0

                if (read > 0 && socket.isConnected()) {
                    try {
                        // Convert PCM 16-bit to μ-law 8-bit
                        val mulawData = pcmToMulaw(pcmBuffer, read)

                        // Base64 encode
                        val base64Chunk = Base64.encodeToString(mulawData, Base64.NO_WRAP)

                        // Send as JSON (Twilio format)
                        val json = """{"event":"media","media":{"payload":"$base64Chunk"}}"""
                        socket.send(json)

                        packetCount++
                        if (packetCount % 100 == 0) {
                            Log.d(TAG, "📤 Sent $packetCount packets (${mulawData.size} μ-law bytes)")
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "❌ Error encoding/sending audio", e)
                    }
                }

                // Small delay to prevent flooding
                delay(20)
            }
        }
    }

    private fun pcmToMulaw(pcmData: ByteArray, length: Int): ByteArray {
        // Convert PCM 16-bit little-endian to μ-law 8-bit
        val buffer = ByteBuffer.wrap(pcmData, 0, length).order(ByteOrder.LITTLE_ENDIAN)
        val numSamples = length / 2
        val mulawData = ByteArray(numSamples)

        for (i in 0 until numSamples) {
            val pcmSample = buffer.short
            mulawData[i] = linearToMulaw(pcmSample)
        }

        return mulawData
    }

    private fun linearToMulaw(pcm: Short): Byte {
        // ITU-T G.711 μ-law compression
        val MULAW_BIAS = 0x84
        val MULAW_MAX = 0x1FFF

        var sample = pcm.toInt()
        val sign = if (sample < 0) 0x80 else 0x00
        if (sign != 0) sample = -sample

        if (sample > MULAW_MAX) sample = MULAW_MAX
        sample += MULAW_BIAS

        var exponent = 7
        var expMask = 0x4000
        while (sample and expMask == 0 && exponent > 0) {
            exponent--
            expMask = expMask shr 1
        }

        val mantissa = (sample shr (exponent + 3)) and 0x0F
        val mulaw = (sign or (exponent shl 4) or mantissa).inv()

        return mulaw.toByte()
    }

    fun stopStreaming() {
        job?.cancel()
        recorder?.stop()
        recorder?.release()
        recorder = null
        Log.d(TAG, "🛑 MicStreamer stopped (total: $packetCount packets)")
    }
}