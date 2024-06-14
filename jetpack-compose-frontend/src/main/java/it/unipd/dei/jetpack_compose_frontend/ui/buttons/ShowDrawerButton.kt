package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import it.unipd.dei.jetpack_compose_frontend.R
import kotlinx.coroutines.launch

@Composable
fun ShowDrawerButton(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    IconButton(onClick = {
        scope.launch {
            drawerState.apply {
                if (isClosed) open() else close()
            }
        }
    }) {
        Icon(imageVector = Icons.Default.Menu, contentDescription = stringResource(id = R.string.drawer_button))
    }
}