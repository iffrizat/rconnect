package io.iffrizat.rconnect.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.iffrizat.rconnect.model.RconConfig
import io.iffrizat.rconnect.model.RconConfigManager

class RconConfigEditorViewModel : ViewModel() {
    val configNameState = mutableStateOf("")
    val configConnectionStringState = mutableStateOf("")
    val configPasswordState = mutableStateOf("")

    companion object {
        const val TAG = "RconConfigEditorViewModel"
    }

    fun setupForConfig(index: Int) {
        if (index < 0) {
            throw IllegalArgumentException("setupForConfig called with negative index")
        }

        try {
            val config = RconConfigManager.getConfig(index)
            configNameState.value = config.name
            configConnectionStringState.value = config.connectionString
            configPasswordState.value = config.password
        } catch (e: Exception) {
            Log.w(TAG, "Failed to setup for config with index $index: $e")
        }
    }

    fun addConfig(index: Int) {
        val new = RconConfig(configNameState.value, configConnectionStringState.value, configPasswordState.value)

        try {
            if (index < 0) {
                RconConfigManager.addConfig(new)
            } else {
                RconConfigManager.updateConfig(index, new)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to save config with index $index: $e")
        }
    }

    fun removeConfig(index: Int) {
        if (index < 0) {
            throw IllegalArgumentException("Called deleteConfig with negative index")
        }

        RconConfigManager.removeConfig(index)
    }
}