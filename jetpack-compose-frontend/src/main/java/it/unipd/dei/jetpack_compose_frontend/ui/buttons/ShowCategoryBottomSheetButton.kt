package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.bottomsheets.CategoryBottomSheet

@Composable
fun ShowCategoryBottomSheetButton(
    category: Category,
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    summaryViewModel: SummaryViewModel
) {

    var showBottomSheet by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            showBottomSheet = true
        },
        modifier = Modifier.padding(end = 15.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,

            contentDescription = stringResource(id = R.string.category)
        )
    }

    if (showBottomSheet) {
        CategoryBottomSheet(
            category = category,
            categoryViewModel = categoryViewModel,
            movementWithCategoryViewModel,
            summaryViewModel
        ) {
            showBottomSheet = false
        }
    }
}