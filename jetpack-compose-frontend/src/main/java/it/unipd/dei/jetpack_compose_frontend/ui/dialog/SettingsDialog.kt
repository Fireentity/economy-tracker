package it.unipd.dei.jetpack_compose_frontend.ui.dialog

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import it.unipd.dei.jetpack_compose_frontend.MainActivity
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.CurrencySettingsButton
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ThemeModeSettingsButton

@Composable
fun SettingsDialog(sharedPreferences: SharedPreferences, onDismiss: () -> Unit) {

    val context = LocalContext.current

    Dialog(
        onDismissRequest = { onDismiss() },
        content = {
            Surface(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CurrencySettingsButton(sharedPreferences)

                    Spacer(modifier = Modifier.height(16.dp))

                    ThemeModeSettingsButton(sharedPreferences = sharedPreferences)

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { onDismiss() }) {
                            Text(text = stringResource(R.string.cancel))
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = {
                            onDismiss()
                            (context as? MainActivity)?.recreate()
                        }) {
                            Text(text = stringResource(id = R.string.confirm))
                        }
                    }
                }
            }
        }
    )
}