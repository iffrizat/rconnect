package io.iffrizat.rconnect.viewmodel

import android.content.res.Resources
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.iffrizat.rconnect.R
import io.iffrizat.rconnect.model.RconConfig
import io.iffrizat.rconnect.protocol.RconPacketReader
import io.iffrizat.rconnect.protocol.RconPacketWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedOutputStream
import java.net.InetSocketAddress
import java.net.Socket

class RconConnectionViewModel : ViewModel() {
    val consoleTextState = mutableStateOf("")
    val inputTextState = mutableStateOf("")

    private val commandChannel = Channel<String>()
    private val socket: Socket = Socket()
    var res: Resources? = null
    private var consoleLocked = true

    companion object {
        const val TAG = "RconConnectionViewModel"
    }

    private fun sendToConsole(message: String) {
        consoleTextState.value += message + '\n'
    }

    fun send() {
        if (consoleLocked) return

        sendToConsole("> " + inputTextState.value)
        consoleLocked = true

        viewModelScope.launch {
            commandChannel.send(inputTextState.value)
            inputTextState.value = ""
            consoleLocked = false
        }
    }

    fun connectToServer(config: RconConfig) {
        try {
            val connectionString = config.connectionString
            val parts = connectionString.split(":")
            val port = parts[1].toInt()

            viewModelScope.launch(Dispatchers.IO) {

                sendToConsole("[RCONNECT] ${res?.getString(R.string.rcon_connection_connection_attempt)} ${config.connectionString}")

                try {
                    socket.connect(InetSocketAddress(parts[0], port), 100)
                    socket.soTimeout = 1000
                } catch (e: Exception) {
                    sendToConsole("[RCONNECT] ${res?.getString(R.string.rcon_connection_connection_failed)}: $e")
                    cancel("Connection failed")
                    return@launch
                }

                Log.i(TAG, "Connected to $connectionString")

                if (!authenticate(config)) {
                    sendToConsole("[RCONNECT] ${res?.getString(R.string.rcon_connection_auth_failed)}")
                    cancel("Auth failed")
                    return@launch
                }

                sendToConsole("[RCONNECT] ${res?.getString(R.string.rcon_connection_connection_successful)} ${config.connectionString}")
                consoleLocked = false
                socket.soTimeout = 0

                viewModelScope.launch(Dispatchers.IO) {
                    senderJob()
                }

                viewModelScope.launch(Dispatchers.IO) {
                    receiverJob()
                }
            }
            Log.i(TAG, "Network jobs started")
        } catch (e: Exception) {
            sendToConsole("[RCONNECT] ${res?.getString(R.string.rcon_connection_preparation_failure)}: $e")
        }
    }

    private fun authenticate(config: RconConfig): Boolean {
        try {
            val os = BufferedOutputStream(socket.getOutputStream())
            RconPacketWriter.writePacket(os, 0xdead, 3, config.password)
            os.flush()
            val packet = RconPacketReader.readPacket(socket.getInputStream())
            Log.i(TAG, "Authentication verdict: ${packet.id == 0xdead && packet.type == 2}")
            return packet.id == 0xdead && packet.type == 2
        } catch (e: Exception) {
            Log.i(TAG, "RCON auth failed to execute: $e")
            return false
        }
    }

    private suspend fun receiverJob() {
        withContext(Dispatchers.IO) {
            Log.i(TAG, "receiverJob starting")
            while (isActive) {
                try {
                    val packet = RconPacketReader.readPacket(socket.getInputStream())
                    if (packet.id != 0xdead || packet.type != 0) continue

                    val toSend = if (packet.body.isEmpty()) res?.getString(R.string.rcon_connection_empty_response) else packet.body
                    sendToConsole("[SERVER] $toSend")
                } catch (e: Exception) {
                    Log.i(TAG, "receiverJob cancelled, reason: $e")
                    sendToConsole("[RCONNECT] ${res?.getString(R.string.rcon_connection_receive_error)}: $e")
                    consoleLocked = true
                    cancel(e.toString())
                }
            }
        }
    }

    private suspend fun senderJob() {
        Log.i(TAG, "senderJob starting")
        withContext(Dispatchers.IO) {
            val os = BufferedOutputStream(socket.getOutputStream())
            while (isActive) {
                val command = commandChannel.receive()
                try {
                    RconPacketWriter.writePacket(os, 0xdead, 0x2, command.trim())
                    os.flush()
                } catch (e: Exception) {
                    Log.i(TAG, "senderJob cancelled, reason: $e")
                    sendToConsole("[RCONNECT] ${res?.getString(R.string.rcon_connection_send_error)}: $e")
                    consoleLocked = true
                    cancel(e.toString())
                }
            }
        }
    }

    fun cleanUp() {
        Log.i(TAG, "Cleaning up")
        viewModelScope.cancel()
        socket.close()
    }
}