package it.unipd.dei.music_application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.music_application.database.BalanceDatabase
import it.unipd.dei.music_application.ui.AppScreen
import it.unipd.dei.music_application.ui.theme.Music_applicationTheme
import it.unipd.dei.music_application.view.CategoryViewModel
import it.unipd.dei.music_application.view.MovementWithCategoryViewModel
import it.unipd.dei.music_application.view.TestViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var db: BalanceDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Music_applicationTheme {
                val movementWithCategoryViewModel: MovementWithCategoryViewModel = hiltViewModel()
                val categoryViewModel: CategoryViewModel = hiltViewModel()
                val testViewModel: TestViewModel = hiltViewModel()

                categoryViewModel.getAllCategories()
                testViewModel.createDummyDataIfNoMovement()
                categoryViewModel.getAllCategories()

                AppScreen(db, movementWithCategoryViewModel, categoryViewModel, testViewModel)
            }
        }
    }
}


