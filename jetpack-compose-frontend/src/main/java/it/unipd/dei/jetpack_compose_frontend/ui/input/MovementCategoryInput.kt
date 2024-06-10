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
import it.unipd.dei.common_backend.viewModels.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementCategoryInput(
    category: String,
    onCategoryChange: (String) -> Unit,
    categoryViewModel: CategoryViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            value = category,
            onValueChange = { onCategoryChange(it) },
            label = { Text("Label") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
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
                        onCategoryChange(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}