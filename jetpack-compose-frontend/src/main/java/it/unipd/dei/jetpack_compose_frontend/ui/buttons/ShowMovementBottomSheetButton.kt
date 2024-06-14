package it.unipd.dei.jetpack_compose_frontend.ui.buttons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.jetpack_compose_frontend.ui.bottomsheets.MovementBottomSheet

@Composable
fun ShowMovementBottomSheetButton(
    movement: MovementWithCategory,
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    summaryViewModel: SummaryViewModel
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            showBottomSheet = true
        }
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            //TODO check here
            contentDescription = "View category"
        )
    }

    if (showBottomSheet) {
        MovementBottomSheet(
            movement,
            categoryViewModel,
            movementWithCategoryViewModel,
            summaryViewModel
        ) {
            showBottomSheet = false
        }
    }
}