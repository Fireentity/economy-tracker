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
import androidx.compose.ui.unit.dp
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.ui.bottomsheets.MovementBottomSheet

@Composable
fun ShowMovementBottomSheetButton(
    modifier: Modifier,
    movement: MovementWithCategory,
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            showBottomSheet = true
        },
        modifier = modifier
            .padding(end = 15.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "View category"
        )
    }

    if (showBottomSheet) {
        MovementBottomSheet(
            movement,
            categoryViewModel,
            movementWithCategoryViewModel
        ) {
            showBottomSheet = false
        }
    }
}