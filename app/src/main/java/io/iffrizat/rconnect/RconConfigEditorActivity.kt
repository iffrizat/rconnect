package io.iffrizat.rconnect

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import io.iffrizat.rconnect.ui.theme.RCONnectTheme
import io.iffrizat.rconnect.view.RconConfigEditor
import io.iffrizat.rconnect.viewmodel.RconConfigEditorViewModel

class RconConfigEditorActivity : ComponentActivity() {
    private val vm by viewModels<RconConfigEditorViewModel>()

    companion object {
        const val TAG = "RconConfigEditorActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val configIndex = intent.getIntExtra("configIndex", -999)
        if (configIndex >= 0) {
            vm.setupForConfig(configIndex)
        }

        Log.i(TAG, "Created with configIndex of $configIndex")
        setContent {
            RCONnectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        RconConfigEditor(vm.configNameState, vm.configConnectionStringState, vm.configPasswordState, configIndex, {
                            vm.addConfig(configIndex)
                            finish()
                        }, {
                            vm.removeConfig(configIndex)
                            finish()
                        })
                    }
                }
            }
        }
    }
}