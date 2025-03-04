package io.iffrizat.rconnect.model

import java.io.Closeable
import java.io.ObjectOutputStream

class RconConfigSerializer(
    private val oos: ObjectOutputStream
) : Closeable {
    fun serializeConfigs(configs: List<RconConfig>) {
        this.oos.writeInt(configs.size)

        for (i in 1..configs.size) {
            this.oos.writeObject(configs[i-1])
        }
    }

    override fun close() {
        this.oos.close()
    }
}