package it.unipd.dei.jetpack_compose_frontend.ui

import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import it.unipd.dei.common_backend.models.BottomNavigationItem
import it.unipd.dei.common_backend.utils.Constants
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.navigation.NavigationDrawer


@Composable
fun AppScreen(
    navController: NavHostController = rememberNavController(),
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    movementWithCategoryViewModel: MovementWithCategoryViewModel = hiltViewModel(),
    summaryViewModel: SummaryViewModel = hiltViewModel(),
    preferences: SharedPreferences
) {
    categoryViewModel.loadAllCategories()
    summaryViewModel.loadSummaryForCurrentMonth()
    summaryViewModel.loadAllSummaries()

    Constants.setCurrency(preferences, stringResource(id = R.string.saved_selected_currency))

    val bottomNavigationIcons = listOf(
        BottomNavigationItem(
            route = stringResource(id = R.string.home_route),
            title = stringResource(id = R.string.home),
            icon = R.drawable.baseline_home_24
        ),
        BottomNavigationItem(
            route = stringResource(id = R.string.register_route),
            title = stringResource(id = R.string.register),
            icon = R.drawable.baseline_bar_chart_24
        ),
        BottomNavigationItem(
            route = stringResource(id = R.string.categories_route),
            title = stringResource(id = R.string.categories),
            icon = R.drawable.baseline_folder_open_24
        ),
    )
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    var selectedIconIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)
                    NavigationBar {
                        bottomNavigationIcons.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = selectedIconIndex == index,
                                onClick = {
                                    selectedIconIndex = index
                                    navController.navigate(item.route)
                                },
                                label = {
                                    Text(text = item.title)
                                },
                                alwaysShowLabel = true,
                                icon = {
                                    Box {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(id = item.icon),
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
                startDestination = stringResource(id = R.string.home),
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(route = bottomNavigationIcons[0].route) {
                    val orientation = LocalConfiguration.current.orientation
                    if (orientation == Configuration.ORIENTATION_PORTRAIT)
                        HomeScreen(summaryViewModel, preferences, drawerState)
                    else
                        NavigationDrawer(drawerState, navController) {
                            HomeScreen(summaryViewModel, preferences, drawerState)
                        }
                }

                composable(route = bottomNavigationIcons[1].route) {

                    val orientation = LocalConfiguration.current.orientation
                    if (orientation == Configuration.ORIENTATION_PORTRAIT)
                        RegisterScreen(
                            categoryViewModel,
                            movementWithCategoryViewModel,
                            summaryViewModel,
                            preferences,
                            drawerState
                        ) else
                        NavigationDrawer(drawerState = drawerState, navController) {
                            RegisterScreen(
                                categoryViewModel,
                                movementWithCategoryViewModel,
                                summaryViewModel,
                                preferences,
                                drawerState
                            )
                        }

                }

                composable(route = bottomNavigationIcons[2].route) {

                    val orientation = LocalConfiguration.current.orientation
                    if (orientation == Configuration.ORIENTATION_PORTRAIT)
                        CategoriesScreen(
                            categoryViewModel,
                            movementWithCategoryViewModel,
                            summaryViewModel,
                            drawerState
                        )
                    else
                        NavigationDrawer(drawerState = drawerState, navController) {
                            CategoriesScreen(
                                categoryViewModel,
                                movementWithCategoryViewModel,
                                summaryViewModel,
                                drawerState
                            )
                        }
                }
            }

        }
    }
}
