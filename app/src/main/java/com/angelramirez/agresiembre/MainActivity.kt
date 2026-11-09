package com.angelramirez.agresiembre

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.angelramirez.agresiembre.ui.navigation.AppNavigation
import com.angelramirez.agresiembre.ui.theme.AgreSiembreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AgreSiembreTheme {
                AppNavigation()
            }
        }
    }
}