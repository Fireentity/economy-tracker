package it.unipd.dei.jetpack_compose_frontend.ui.bottomsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowAddCategoryDialogButton
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowDeleteCategoryDialogButton
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowEditCategoryDialogButton
import it.unipd.dei.jetpack_compose_frontend.ui.cards.CategoryCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBottomSheet(
    category: Category,
    categoryViewModel: CategoryViewModel,
) {
    ModalBottomSheet(onDismissRequest = { }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {

            CategoryCard(category) {}

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
            )

            ShowAddCategoryDialogButton(
                categoryViewModel = categoryViewModel
            )

            ShowEditCategoryDialogButton(categoryViewModel, category)

            ShowDeleteCategoryDialogButton(categoryViewModel, category)
        }
    }
}