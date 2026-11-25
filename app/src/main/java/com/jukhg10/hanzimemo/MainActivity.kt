package com.jukhg10.hanzimemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jukhg10.hanzimemo.data.preferences.UserPreferencesManager
import com.jukhg10.hanzimemo.ui.i18n.LocalAppLanguage
import com.jukhg10.hanzimemo.ui.i18n.LocalAppStrings
import com.jukhg10.hanzimemo.ui.i18n.getStringsForLanguage
import com.jukhg10.hanzimemo.ui.navigation.AppNavigation
import com.jukhg10.hanzimemo.ui.theme.HanziMemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val preferencesManager = UserPreferencesManager.getInstance(this)

        setContent {
            val themeMode by preferencesManager.themeMode.collectAsState()
            val language by preferencesManager.language.collectAsState()
            val strings = remember(language) { getStringsForLanguage(language) }

            HanziMemoTheme(themeMode = themeMode) {
                CompositionLocalProvider(
                    LocalAppStrings provides strings,
                    LocalAppLanguage provides language
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AppNavigation()
                    }
                }
            }
        }
    }
}
