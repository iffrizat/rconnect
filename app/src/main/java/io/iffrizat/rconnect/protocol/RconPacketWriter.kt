package io.iffrizat.rconnect.protocol

import java.io.ByteArrayOutputStream
import java.io.OutputStream

class RconPacketWriter(private val out: OutputStream) {
    private val intBuf = ByteArray(Int.SIZE_BYTES)

    companion object {
        fun writePacket(o: OutputStream, id: Int, type: Int, body: String) {
            val buf = ByteArrayOutputStream(4 + 4 + body.length + 1 + 1)
            val writer = RconPacketWriter(buf)
            writer.writePacket(id, type, body)

            o.write(buf.toByteArray())
        }
    }

    private fun writeInt(i: Int) {
        intBuf[0] = (i shr 0).toByte()
        intBuf[1] = (i shr 8).toByte()
        intBuf[2] = (i shr 16).toByte()
        intBuf[3] = (i shr 24).toByte()

        out.write(intBuf)
    }

    private fun writeString(s: String) {
        val bytes = ByteArray(s.length + 1)
        bytes[bytes.size-1] = 0x00

        s.toByteArray(Charsets.US_ASCII).copyInto(bytes)

        out.write(bytes)
    }

    fun writePacket(id: Int, type: Int, body: String) {
        // Size is: id (4 bytes) + type (4 bytes) + body (l + 1 bytes) + null byte (1 byte)
        writeInt(4 + 4 + body.length + 1 + 1)

        // Id
        writeInt(id)

        // Type
        writeInt(type)

        // Body
        writeString(body)

        // Null
        out.write(0x00)
    }
}