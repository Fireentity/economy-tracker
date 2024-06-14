package it.unipd.dei.jetpack_compose_frontend.ui.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowCategoryBottomSheetButton

@Composable
fun CategoryCard(
    category: Category,
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel
) {
    val icon = ImageVector.vectorResource(id = R.drawable.baseline_folder_open_24)
    val background: Color = colorResource(id = R.color.blue_100)
    val colorFilter = ColorFilter.tint(colorResource(id = R.color.blue_700))

    ListItem(
        headlineContent = { Text(category.identifier) },
        leadingContent = {
            Image(
                painter = rememberVectorPainter(icon),
                contentDescription = "Category card icon",
                colorFilter = colorFilter,
                modifier = Modifier
                    .background(
                        background,
                        CircleShape
                    )
                    .padding(10.dp)
            )
        },
        trailingContent = {
            ShowCategoryBottomSheetButton(
                category,
                categoryViewModel,
                movementWithCategoryViewModel
            )
        }
    )
}