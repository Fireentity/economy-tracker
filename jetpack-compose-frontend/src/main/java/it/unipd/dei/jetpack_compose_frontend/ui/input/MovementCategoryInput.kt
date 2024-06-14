package it.unipd.dei.jetpack_compose_frontend.ui.input

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementCategoryInput(
    category: String,
    onCategoryChange: (String) -> Unit,
    categoryViewModel: CategoryViewModel
) {
    var error by remember { mutableStateOf(false) }
    val onChange: (String) -> Unit = {
        onCategoryChange(it)
        error = categoryViewModel.getCategoryByIdentifier(it) == null
    }
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            readOnly = true,
            modifier = Modifier.menuAnchor(),
            value = category,
            onValueChange = {
                onChange(it)
            },
            label = { Text(stringResource(id = R.string.category)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            isError = error
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            categoryViewModel.allCategories.value?.keys?.forEach { selectionOption ->
                DropdownMenuItem(
                    modifier = Modifier.menuAnchor(),
                    text = { Text(text = selectionOption) },
                    onClick = {
                        onChange(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}