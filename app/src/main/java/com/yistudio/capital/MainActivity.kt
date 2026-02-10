package com.yistudio.capital

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.yistudio.capital.engine.TaskEngine
import com.yistudio.capital.main.MainNavGraph
import com.yistudio.capital.ui.theme.CapitalTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val level: MutableList<Int> = mutableListOf()
        enableEdgeToEdge()
        setContent {
            CapitalTheme {
                MainNavGraph()
            }
        }
    }
}