package io.iffrizat.rconnect.protocol

import java.io.ByteArrayOutputStream
import java.io.InputStream

class RconPacketReader(private val input: InputStream) {
    companion object {
        fun readPacket(input: InputStream): RconPacketData {
            val packetLength = RconPacketReader(input).readInt()
            // As per RCON standards
            if (packetLength > 4096 || packetLength < 10) {
                throw IllegalStateException("RCON packet length out of bounds: $packetLength")
            }
            val packetBytes = input.readNBytes(packetLength)
            val reader = RconPacketReader(packetBytes.inputStream())

            val id = reader.readInt()
            val type = reader.readInt()
            val body = reader.readString()

            return RconPacketData(id, type, body)
        }
    }

    fun readInt(): Int {
        val bytes = input.readNBytes(4)
        return (bytes[0].toUByte().toUInt() + (bytes[1].toUByte()
            .toUInt() shl 8) + (bytes[2].toUByte().toUInt() shl 16) + (bytes[2].toUByte()
            .toUInt() shl 24)).toInt()
    }

    fun readString(): String {
        val buf = ByteArrayOutputStream()
        while (true) {
            val read = input.read()
            if (read == 0x00) break
            buf.write(read)
        }

        return buf.toString(Charsets.US_ASCII)
    }
}