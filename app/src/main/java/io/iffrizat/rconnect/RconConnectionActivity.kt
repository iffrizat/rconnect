package io.iffrizat.rconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import io.iffrizat.rconnect.model.RconConfigManager
import io.iffrizat.rconnect.ui.theme.RCONnectTheme
import io.iffrizat.rconnect.view.RconConsole
import io.iffrizat.rconnect.viewmodel.RconConnectionViewModel

class RconConnectionActivity : ComponentActivity() {
    private val vm by viewModels<RconConnectionViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val configIndex = intent.getIntExtra("configIndex", -1)
        vm.res = resources
        vm.connectToServer(RconConfigManager.getConfig(configIndex))

        setContent {
            RCONnectTheme {
                Scaffold { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        RconConsole(vm.consoleTextState, vm.inputTextState, {vm.send()}, {finish()})
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.cleanUp()
    }
}