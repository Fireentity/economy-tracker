package it.unipd.dei.jetpack_compose_frontend.ui.bottomsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowDeleteCategoryDialogButton
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowEditCategoryDialogButton
import it.unipd.dei.jetpack_compose_frontend.ui.cards.CategoryCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryBottomSheet(
    category: Category,
    categoryViewModel: CategoryViewModel,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            CategoryCard(category, categoryViewModel)

            ShowDeleteCategoryDialogButton(categoryViewModel, category)

            ShowEditCategoryDialogButton(categoryViewModel, category)

            Spacer(modifier = Modifier.fillMaxWidth().height(30.dp))
        }
    }
}