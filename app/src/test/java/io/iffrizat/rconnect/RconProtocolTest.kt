package io.iffrizat.rconnect

import io.iffrizat.rconnect.protocol.RconPacketReader
import io.iffrizat.rconnect.protocol.RconPacketWriter
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.net.Socket

class RconProtocolTest {
    @Test
    fun rconWriterTest() {
        val out = ByteArrayOutputStream()
        val writer = RconPacketWriter(out)
        writer.writePacket(0x11, 0x22, "Hello!")

        val expected = byteArrayOf(0x10, 0x00, 0x00, 0x00, 0x11, 0x00, 0x00, 0x00, 0x22, 0x00, 0x00, 0x00, 0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x21, 0x00, 0x00)
        assert(expected.contentEquals(out.toByteArray()))
    }

    @Test
    fun rconReaderTest() {
        val packet = byteArrayOf(0x10, 0x00, 0x00, 0x00, 0x11, 0x00, 0x00, 0x00, 0x22, 0x00, 0x00, 0x00, 0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x21, 0x00, 0x00)
        val reader = RconPacketReader(packet.inputStream())

        // Size
        assert(reader.readInt() == 0x10)

        // Id
        assert(reader.readInt() == 0x11)

        // Type
        assert(reader.readInt() == 0x22)

        // Body
        assert(reader.readString() == "Hello!")
    }

    @Test
    fun rconConnectionTest() {
        val socket = Socket("192.168.2.69", 25575)

        RconPacketWriter.writePacket(socket.getOutputStream(), 0xdead, 3, "111")
        val response = RconPacketReader.readPacket(socket.getInputStream())
        assert(response.id == 0xdead)
        assert(response.type == 2)
        socket.close()
    }

    @Test
    fun rconFailedConnectionTest() {
        val socket = Socket("192.168.2.69", 25575)

        RconPacketWriter.writePacket(socket.getOutputStream(), 0xdead, 3, "222")
        val response = RconPacketReader.readPacket(socket.getInputStream())
        assert(response.id == -1)
        assert(response.type == 2)
        socket.close()
    }
}