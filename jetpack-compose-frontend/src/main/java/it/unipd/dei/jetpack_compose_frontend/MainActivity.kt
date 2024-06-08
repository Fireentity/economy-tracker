package it.unipd.dei.jetpack_compose_frontend

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.database.BalanceDatabase
import it.unipd.dei.jetpack_compose_frontend.ui.AppScreen
import it.unipd.dei.jetpack_compose_frontend.ui.theme.EconomyTrackerTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var db: BalanceDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_EconomyTracker)
        setContent {
            EconomyTrackerTheme {
                AppScreen(db)
            }
        }
    }
}


