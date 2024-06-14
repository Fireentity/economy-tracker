package it.unipd.dei.jetpack_compose_frontend

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.res.stringResource
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.jetpack_compose_frontend.ui.AppScreen
import it.unipd.dei.jetpack_compose_frontend.ui.theme.EconomyTrackerTheme

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_EconomyTracker)

        setContent {
            val isDarkTheme = this.getPreferences(MODE_PRIVATE).getBoolean(stringResource(id = R.string.saved_selected_theme), isSystemInDarkTheme())
            EconomyTrackerTheme(isDarkTheme) {
                AppScreen(preferences = this.getPreferences(MODE_PRIVATE))
            }
        }
    }

}

