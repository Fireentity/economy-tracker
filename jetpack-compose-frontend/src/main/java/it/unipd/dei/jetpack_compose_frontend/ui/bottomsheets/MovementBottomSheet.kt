package it.unipd.dei.jetpack_compose_frontend.ui.bottomsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowDeleteMovementDialogButton
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowEditMovementDialogButton
import it.unipd.dei.jetpack_compose_frontend.ui.cards.MovementCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementBottomSheet(
    movement: MovementWithCategory,
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    onDismissRequest: () -> Unit

) {
    ModalBottomSheet(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {

            MovementCard(movement, categoryViewModel, movementWithCategoryViewModel)

            ShowDeleteMovementDialogButton(movementWithCategoryViewModel, movement)

            ShowEditMovementDialogButton(categoryViewModel, movementWithCategoryViewModel, movement)

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(30.dp))
        }
    }
}