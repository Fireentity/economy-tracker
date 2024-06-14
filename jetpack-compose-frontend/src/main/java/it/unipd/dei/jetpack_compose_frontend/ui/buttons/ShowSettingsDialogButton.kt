package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import android.content.SharedPreferences
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.dialog.SettingsDialog

@Composable
fun ShowSettingsDialogButton(sharedPreferences: SharedPreferences) {
    var showDialog: Boolean by remember { mutableStateOf(false) }

    IconButton(onClick = { showDialog = true }) {
        Icon(imageVector = Icons.Filled.Settings, contentDescription = stringResource(id = R.string.settings))
    }

    if(showDialog) {
        SettingsDialog(sharedPreferences = sharedPreferences) {
            showDialog = false
        }
    }
}