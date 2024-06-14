package it.unipd.dei.jetpack_compose_frontend.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun NavigationDrawer(drawerState: DrawerState, navController: NavHostController, content: @Composable () -> Unit) {
    val homeRoute = stringResource(id = R.string.home_route)
    val categoriesRoute = stringResource(id = R.string.categories_route)
    val registerRoute = stringResource(id = R.string.register_route)
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                    NavigationDrawerItem(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        label = { Text(text = stringResource(id = R.string.home)) },
                        icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.baseline_home_24), contentDescription = "") },
                        selected = false,
                        onClick = { navController.navigate(homeRoute) }
                    )
                    NavigationDrawerItem(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        label = { Text(text = stringResource(id = R.string.register)) },
                        icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.baseline_bar_chart_24), contentDescription = "") },
                        selected = false,
                        onClick = { navController.navigate(registerRoute) }
                    )
                    NavigationDrawerItem(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        label = { Text(text = stringResource(id = R.string.categories)) },
                        icon = { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.baseline_folder_open_24), contentDescription = "") },
                        selected = false,
                        onClick = { navController.navigate(categoriesRoute) }
                    )
                }
            }
        },
    ) {
        content()
    }
}