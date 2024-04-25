package it.unipd.dei.music_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import it.unipd.dei.music_application.ui.theme.Music_applicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Music_applicationTheme {
                AppScreen()
            }
        }
    }
}


