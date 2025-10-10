//package com.apoorv.myvoice.socket
//
//import android.annotation.SuppressLint
//import android.media.AudioFormat
//import android.media.AudioManager
//import android.media.AudioTrack
//import android.util.Log
//import java.util.Arrays
//
//class AudioPlayer(sampleRate: Int = 16000) {
//
//    private val TAG = "CallActivity"
//
//    private val minBufferSize = AudioTrack.getMinBufferSize(
//        sampleRate,
//        AudioFormat.CHANNEL_OUT_MONO,
//        AudioFormat.ENCODING_PCM_16BIT
//    )
//
//    private val audioTrack = AudioTrack(
//        AudioManager.STREAM_MUSIC,
//        sampleRate,
//        AudioFormat.CHANNEL_OUT_MONO,
//        AudioFormat.ENCODING_PCM_16BIT,
//        minBufferSize,
//        AudioTrack.MODE_STREAM
//    )
//
//    // Temp buffer to accumulate chunks
//    private val bufferQueue = mutableListOf<Byte>()
//
//    init {
//        audioTrack.play()
//        Log.d(TAG, "🎧 AudioPlayer initialized with sampleRate=$sampleRate, bufferSize=$minBufferSize")
//    }
//
//    @Synchronized
//    fun playAudio(pcmData: ByteArray) {
//        if (pcmData.isEmpty()) return
//
//        // Add incoming bytes to queue
//        bufferQueue.addAll(pcmData.toList())
//
//        // Only write to AudioTrack when we have enough data
//        if (bufferQueue.size >= 1024) { // 1024 bytes per write, adjust as needed
//            val writeBuffer = ByteArray(1024)
//            for (i in 0 until 1024) writeBuffer[i] = bufferQueue.removeAt(0)
//            audioTrack.write(writeBuffer, 0, writeBuffer.size)
//            Log.d(TAG, "🔊 Played 1024 bytes of audio")
//        }
//    }
//
//    @Synchronized
//    fun flush() {
//        // Play remaining bytes if any
//        if (bufferQueue.isNotEmpty()) {
//            val remaining = ByteArray(bufferQueue.size)
//            for (i in remaining.indices) remaining[i] = bufferQueue[i]
//            audioTrack.write(remaining, 0, remaining.size)
//            bufferQueue.clear()
//            Log.d(TAG, "🔊 Flushed remaining ${remaining.size} bytes")
//        }
//    }
//
//    fun stop() {
//        flush()
//        audioTrack.stop()
//        audioTrack.release()
//        Log.d(TAG, "🛑 AudioPlayer stopped")
//    }
//}
package com.apoorv.myvoice.socket

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder

class AudioPlayer(private val sampleRate: Int = 8000) { // Changed to 8kHz for Twilio

    private val TAG = "AudioPlayer"

    private val minBufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )

    private val audioTrack = AudioTrack(
        AudioManager.STREAM_VOICE_CALL,
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT,
        minBufferSize * 4,
        AudioTrack.MODE_STREAM
    )

    private var packetCount = 0

    init {
        // Set maximum volume
        val maxVol = AudioTrack.getMaxVolume()
        audioTrack.setStereoVolume(maxVol, maxVol)
        audioTrack.play()
        Log.d(TAG, "🎧 AudioPlayer initialized: sampleRate=$sampleRate, buffer=$minBufferSize, maxVol=$maxVol")
    }

    @Synchronized
    fun playAudio(pcmData: ByteArray) {
        if (pcmData.isEmpty()) return

        try {
            // Decode µ-law to PCM for 160-byte packets (Twilio standard)
            val processedData = if (pcmData.size == 160) {
                mulawToPcm(pcmData)
            } else {
                pcmData
            }

            val written = audioTrack.write(processedData, 0, processedData.size)

            packetCount++
            if (packetCount % 100 == 0) {
                Log.d(TAG, "🔊 Packets: $packetCount, last: ${processedData.size} bytes")
            }

            if (written < 0) {
                Log.e(TAG, "❌ Write failed: $written")
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error playing audio", e)
        }
    }

    private fun mulawToPcm(mulawData: ByteArray): ByteArray {
        // Convert 8-bit µ-law to 16-bit PCM
        val pcmData = ByteArray(mulawData.size * 2)
        val buffer = ByteBuffer.wrap(pcmData).order(ByteOrder.LITTLE_ENDIAN)

        for (mulaw in mulawData) {
            buffer.putShort(mulawToLinear(mulaw))
        }

        return pcmData
    }

    private fun mulawToLinear(mulaw: Byte): Short {
        // Standard ITU-T G.711 µ-law decompression
        val MULAW_BIAS = 0x84
        val ulawbyte = mulaw.toInt() and 0xFF
        val ulawbyte_inv = ulawbyte xor 0xFF

        val exponent = (ulawbyte_inv shr 4) and 0x07
        val mantissa = ulawbyte_inv and 0x0F

        var sample = (mantissa shl (exponent + 3)) + (MULAW_BIAS shl exponent)

        if (ulawbyte and 0x80 == 0) sample = -sample

        return sample.toShort()
    }

    @Synchronized
    fun flush() {
        audioTrack.flush()
        Log.d(TAG, "🔊 AudioTrack flushed")
    }

    fun stop() {
        try {
            audioTrack.pause()
            audioTrack.flush()
            audioTrack.stop()
            audioTrack.release()
            Log.d(TAG, "🛑 AudioPlayer stopped (total: $packetCount packets)")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Error stopping", e)
        }
    }
}
