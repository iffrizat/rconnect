package io.iffrizat.rconnect.model

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object RconConfigManager {
    var configs = mutableStateListOf<RconConfig>()
    private const val TAG = "RconConfigManager"

    fun readConfigsFromFile(source: File) {
        Log.i(TAG, "Reading configs from ${source.path}")
        val loader = RconConfigLoader(ObjectInputStream(source.inputStream()))
        configs.removeAll(configs)
        loader.use {
            configs.addAll(loader.readConfigs())
        }
    }

    fun getConfig(index: Int): RconConfig {
        return configs[index].copy()
    }

    fun updateConfig(index: Int, new: RconConfig) {
        configs[index] = new
    }

    fun addConfig(new: RconConfig) {
        configs.add(new)
    }

    fun removeConfig(index: Int) {
        configs.removeAt(index)
    }

    fun storeConfigsInFile(destination: File) {
        Log.i(TAG, "Storing configs in ${destination.path}")
        val serializer = RconConfigSerializer(ObjectOutputStream(destination.outputStream()))
        serializer.use {
            serializer.serializeConfigs(configs)
        }
    }
}