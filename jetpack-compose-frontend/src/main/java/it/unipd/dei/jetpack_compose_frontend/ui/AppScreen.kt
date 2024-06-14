package it.unipd.dei.jetpack_compose_frontend.ui

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import it.unipd.dei.common_backend.viewModels.TestViewModel
import it.unipd.dei.jetpack_compose_frontend.R


@Composable
fun AppScreen(
    navController: NavHostController = rememberNavController(),
    testViewModel: TestViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel = hiltViewModel(),
    movementWithCategoryViewModel: MovementWithCategoryViewModel = hiltViewModel(),
    summaryViewModel: SummaryViewModel = hiltViewModel(),
    preferences: SharedPreferences
) {
    testViewModel.createDummyDataIfNoMovement()
    categoryViewModel.loadAllCategories()
    summaryViewModel.loadSummaryForCurrentMonth()
    summaryViewModel.loadAllSummaries()

    Constants.setCurrency(preferences, stringResource(id = R.string.saved_selected_currency))

    val bottomNavigationIcons = listOf(
        BottomNavigationItem(
            route = "home",
            title = stringResource(id = R.string.home),
            icon = R.drawable.baseline_home_24
        ),
        BottomNavigationItem(
            route = "register",
            title = stringResource(id = R.string.register),
            icon = R.drawable.baseline_bar_chart_24
        ),
        BottomNavigationItem(
            route = "categories",
            title = stringResource(id = R.string.categories),
            icon = R.drawable.baseline_folder_open_24
        ),
    )

    var selectedIconIndex by rememberSaveable {
        mutableIntStateOf(0)
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
                    HomeScreen(summaryViewModel, preferences)
                }

                composable(route = bottomNavigationIcons[1].route) {
                    RegisterScreen(
                        categoryViewModel,
                        movementWithCategoryViewModel,
                        summaryViewModel,
                        preferences
                    )
                }

                composable(route = bottomNavigationIcons[2].route) {
                    CategoriesScreen(
                        categoryViewModel,
                        movementWithCategoryViewModel,
                        summaryViewModel
                    )
                }
            }

        }
    }
}
