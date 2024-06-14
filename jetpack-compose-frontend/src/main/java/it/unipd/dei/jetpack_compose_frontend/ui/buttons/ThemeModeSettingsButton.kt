package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun ThemeModeSettingsButton(sharedPreferences: SharedPreferences) {

    val themeSharedPreferenceKey = stringResource(id = R.string.saved_selected_theme)
    var isThemeSwitchOn: Boolean by remember {
        mutableStateOf(
            sharedPreferences.getBoolean("isThemeSwitchOn", false)
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.dark_mode),
            modifier = Modifier
                .padding(vertical = 15.dp)
                .weight(1f)
        )
        Switch(
            checked = isThemeSwitchOn,
            onCheckedChange = {
                isThemeSwitchOn = !isThemeSwitchOn
                sharedPreferences.edit()
                    .putBoolean(themeSharedPreferenceKey, isThemeSwitchOn)
                    .apply()
            }
        )
    }
}

