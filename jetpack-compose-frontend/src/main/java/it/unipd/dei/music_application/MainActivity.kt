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

    /*
    val db = Room.databaseBuilder(
        applicationContext,
        BalanceDatabase::class.java, "balance.db"
    ).build()
    */

    /*private val categoryViewModel by viewModels<CategoryTotalViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                fun <CategoryTotalViewModel : ViewModel?> create(modelClass: Class<it.unipd.dei.music_application.view.CategoryTotalViewModel>): it.unipd.dei.music_application.view.CategoryTotalViewModel {
                    return CategoryTotalViewModel(db.getCategoryDao())
                }
            }
        }
    )*/

    /*private val movementViewModel by viewModels<MovementWithCategoryViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                fun <T : ViewModel?> create(modelClass: Class<T>): T {
                    return MovementWithCategoryViewModel(db.getMovementDao()) as T
                }
            }
        }
    )*/

    /*private val testViewModel by viewModels<TestViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                fun <TestViewModel : ViewModel?> create(modelClass: Class<it.unipd.dei.music_application.view.TestViewModel>): it.unipd.dei.music_application.view.TestViewModel {
                    return TestViewModel(db.getBalanceDao())
                }
            }
        }
    )*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Music_applicationTheme {
                AppScreen(db)
            }
        }
    }
}


