package io.iffrizat.rconnect.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import io.iffrizat.rconnect.model.RconConfigManager
import java.io.File

class RconConfigPickerViewModel : ViewModel() {
    val configsState = RconConfigManager.configs

    companion object {
        const val TAG = "RconConfigPickerViewModel"
    }

    fun initializeConfigs(source: File) {
        try {
            if (!source.exists()) {
                Log.i(TAG, "${source.name} doesn't exist, creating the file")
                source.createNewFile()
            }

            RconConfigManager.readConfigsFromFile(source)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to initialize configs: ${e.message}")
        }
    }

    fun saveConfigs(destination: File) {
        try {
            if (!destination.exists()) {
                Log.i(TAG, "${destination.name} doesn't exist, creating the file")
                destination.createNewFile()
            }

            RconConfigManager.storeConfigsInFile(destination)
        } catch (e: Exception) {
            Log.w(TAG, "Failed to save configs: ${e.message}")
        }
    }
}