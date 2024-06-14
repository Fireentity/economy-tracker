package it.unipd.dei.jetpack_compose_frontend

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.jetpack_compose_frontend.ui.AppScreen
import it.unipd.dei.jetpack_compose_frontend.ui.theme.EconomyTrackerTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_EconomyTracker)

        setContent {
            EconomyTrackerTheme {
                AppScreen(preferences = this.getPreferences(MODE_PRIVATE))
            }
        }
    }

}

