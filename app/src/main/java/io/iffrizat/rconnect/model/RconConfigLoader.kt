package io.iffrizat.rconnect.model

import java.io.Closeable
import java.io.ObjectInputStream

class RconConfigLoader(
    private val ois: ObjectInputStream
) : Closeable {
    fun readConfigs() : List<RconConfig> {
        val result: MutableList<RconConfig> = mutableListOf()
        val configCount = this.ois.readInt()

        if (configCount < 0) throw IllegalStateException("Read config count is negative")

        for (i in 1..configCount) {
            val config = this.ois.readObject()
            if (config is RconConfig) {
                result.add(config)
            } else {
                throw IllegalStateException("Expected to read ${RconConfig::class}, read ${config::class} instead")
            }
        }

        return result
    }

    override fun close() {
        this.ois.close()
    }
}