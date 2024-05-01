package it.unipd.dei.music_application

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.unipd.dei.music_application.ui.HomeScreen
import it.unipd.dei.music_application.ui.RecurrencesScreen
import it.unipd.dei.music_application.ui.RegisterScreen


enum class AppScreen() {
    Home,
    Registro,
    Ricorrenze
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)


@Composable
fun AppScreen(
    //viewModel: OrderViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val bottomNavigationIcons = listOf(
        BottomNavigationItem(
            title = AppScreen.entries[0].name,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
        ),
        BottomNavigationItem(
            title = AppScreen.entries[1].name,
            selectedIcon = Icons.Filled.Create,
            unselectedIcon = Icons.Outlined.Create,
        ),
        BottomNavigationItem(
            title = AppScreen.entries[2].name,
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search,
        ),
    )

    var selectedIconIndex by rememberSaveable {
        mutableStateOf(0)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    bottomNavigationIcons.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedIconIndex == index,
                            onClick = {
                                selectedIconIndex = index
                                navController.navigate(item.title)
                            },
                            label = {
                                Text(text = item.title)
                            },
                            alwaysShowLabel = true,
                            icon = {
                                Box {
                                    Icon(
                                        imageVector = if(index == selectedIconIndex) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title
                                    )
                                }
                            }
                        )
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = AppScreen.entries[0].name, // Start page (Home) at app launch
                modifier = Modifier.padding(paddingValues)
            ) {
                // Home
                composable(route = AppScreen.entries[0].name) {
                    HomeScreen(modifier = Modifier.fillMaxSize())
                }

                // Registro
                composable(route = AppScreen.entries[1].name) {
                    RegisterScreen(modifier = Modifier.fillMaxSize())
                }

                // Ricorrenze
                composable(route = AppScreen.entries[2].name) {
                    RecurrencesScreen(modifier = Modifier.fillMaxSize())
                }
            }

        }
    }
}
