package it.unipd.dei.jetpack_compose_frontend.ui.dialog

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import it.unipd.dei.common_backend.models.MovementBuilder
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.AddMovementButton
import it.unipd.dei.jetpack_compose_frontend.ui.input.MovementCategoryInput
import it.unipd.dei.jetpack_compose_frontend.ui.input.MovementDateInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertMovementDialog(
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    movement: MovementWithCategory? = null,
    onDismiss: () -> Unit = {},
) {

    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    val movementBuilder by remember {
        mutableStateOf(MovementBuilder(
            { amount },
            { category },
            { date }
        ))
    }
    val regex = Regex("^(-)?\\d{0,6}(\\.\\d{0,2})?$");

    BasicAlertDialog(
        onDismissRequest = { onDismiss() }
    ) {
        Surface(shape = RoundedCornerShape(24.dp)) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(24.dp),
            ) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = stringResource(R.string.create_movement),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        if (it.matches(regex)) {
                            amount = it
                        }
                    },
                    label = { Text(stringResource(R.string.insert_movement_amount)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                Spacer(modifier = Modifier.height(8.dp))

                MovementCategoryInput(category, { category = it }, categoryViewModel)

                Spacer(modifier = Modifier.height(8.dp))

                MovementDateInput(LocalContext.current, date) { date = it }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text(stringResource(R.string.cancel))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    AddMovementButton(
                        movementBuilder = movementBuilder,
                        movementWithCategoryViewModel,
                        categoryViewModel
                    )
                }
            }
        }
    }
}
