package it.unipd.dei.jetpack_compose_frontend.ui.input

import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun CategoryFilterInput(
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    categoryViewModel: CategoryViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    DropdownMenu(modifier= Modifier.heightIn(max = 300.dp, min = 0.dp), expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(
            text = {
                Text(stringResource(id = R.string.all))
            },
            onClick = { movementWithCategoryViewModel.addCategoryFilter(null) }
        )
        categoryViewModel.allCategories.value?.forEach { category ->
            DropdownMenuItem(
                text = {
                    Text(category.value.identifier)
                },
                onClick = { movementWithCategoryViewModel.addCategoryFilter(category.value) }
            )
        }
    }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.baseline_filter_alt_off_24),
            contentDescription = "Localized description"
        )
    }
}