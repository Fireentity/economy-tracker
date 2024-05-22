package it.unipd.dei.music_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import it.unipd.dei.music_application.database.BalanceDatabase
import it.unipd.dei.music_application.ui.theme.Music_applicationTheme
import it.unipd.dei.music_application.view.CategoryTotalViewModel
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel
import it.unipd.dei.music_application.view.TestViewModel

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            BalanceDatabase::class.java,
            "balance.db"
        ).build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Music_applicationTheme {
                AppScreen(db)
            }
        }
    }
}


