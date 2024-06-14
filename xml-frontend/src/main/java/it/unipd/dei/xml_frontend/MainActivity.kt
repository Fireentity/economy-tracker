package it.unipd.dei.xml_frontend


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.database.BalanceDatabase
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.TestViewModel
import it.unipd.dei.xml_frontend.fragments.RegisterFragment.Companion.DEFAULT_TAB_SELECTED
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var database: BalanceDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.findNavController();
        bottomNavigationView.setupWithNavController(navController)

        val sharedPref = getPreferences(MODE_PRIVATE)

        try {
            val isDarkModeEnable =
                sharedPref.getBoolean(getString(R.string.is_dark_mode_enable), false)
            if(isDarkModeEnable){
                setTheme(R.style.Theme_EconomyTracker_Dark)
            }
        } catch (_: ClassCastException) {
        }
    }
}