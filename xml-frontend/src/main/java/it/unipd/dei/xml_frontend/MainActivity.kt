package it.unipd.dei.xml_frontend


import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import it.unipd.dei.common_backend.database.BalanceDatabase
import it.unipd.dei.common_backend.utils.Constants
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var database: BalanceDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = getPreferences(MODE_PRIVATE)

        val isDarkModeEnable =
            sharedPref.getBoolean(getString(R.string.is_dark_mode_enable), false)
        if (isDarkModeEnable) {
            setTheme(R.style.Theme_EconomyTracker_Dark)
        }
        Constants.setCurrency(sharedPref, resources.getString(R.string.saved_selected_currency))


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.findNavController();
        setContentView(R.layout.activity_main)
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
            bottomNavigationView.setupWithNavController(navController)
        } else {
            val navigationView: NavigationView = findViewById(R.id.navigation_view)
            navigationView.setupWithNavController(navController)
        }

    }
}