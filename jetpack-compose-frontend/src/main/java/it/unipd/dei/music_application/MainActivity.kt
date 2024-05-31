package it.unipd.dei.music_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.database.BalanceDatabase
import it.unipd.dei.music_application.ui.AppScreen
import it.unipd.dei.music_application.ui.theme.Music_applicationTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var db: BalanceDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Music_applicationTheme {
                AppScreen(db)
            }
        }
    }
}


