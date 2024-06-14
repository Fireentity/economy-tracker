package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import it.unipd.dei.common_backend.enums.Currency
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun CurrencySettingsButton(
    sharedPreferences: SharedPreferences
) {
    val currencySharedPreferenceKey = stringResource(id = R.string.saved_selected_currency)
    val radioOptions = Currency.entries
    var selectedOption: Int by remember {
        mutableIntStateOf(
            sharedPreferences.getInt(
                currencySharedPreferenceKey,
                radioOptions[0].value
            )
        )
    }

    Text(text = stringResource(id = R.string.choose_the_currency_for_your_movements))
    Column(Modifier.selectableGroup()) {
        RadioButtonEntry(
            selectedOption,
            sharedPreferences,
            Currency.EURO.value,
            ImageVector.vectorResource(R.drawable.baseline_euro_24),
            stringResource(id = R.string.euro)
        ) {
            selectedOption = Currency.EURO.value
        }
        RadioButtonEntry(
            selectedOption,
            sharedPreferences,
            Currency.DOLLAR.value,
            ImageVector.vectorResource(R.drawable.baseline_dollar_24),
            stringResource(id = R.string.dollar)
        ) {
            selectedOption = Currency.DOLLAR.value
        }
        RadioButtonEntry(
            selectedOption,
            sharedPreferences,
            Currency.POUND.value,
            ImageVector.vectorResource(R.drawable.baseline_pound_24),
            stringResource(id = R.string.pound)
        ) {
            selectedOption = Currency.POUND.value
        }
    }
}

@Composable
fun RadioButtonEntry(
    selectedCurrency: Int,
    sharedPreferences: SharedPreferences,
    currency: Int,
    imageVector: ImageVector,
    text: String,
    onSelect: () -> Unit
) {
    val currencySharedPreferenceKey = stringResource(id = R.string.saved_selected_currency)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .selectable(
                selected = (currency == selectedCurrency),
                onClick = {
                    onSelect()
                    sharedPreferences
                        .edit()
                        .putInt(currencySharedPreferenceKey, currency)
                        .apply()
                },
                role = Role.RadioButton
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (currency == selectedCurrency),
            onClick = null
        )
        Icon(
            modifier = Modifier.padding(horizontal = 5.dp),
            imageVector = imageVector,
            contentDescription = ""
        )
        Text(text = text)
    }
}
