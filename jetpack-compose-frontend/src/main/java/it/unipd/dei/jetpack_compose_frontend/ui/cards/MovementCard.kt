package it.unipd.dei.jetpack_compose_frontend.ui.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.utils.Constants
import it.unipd.dei.common_backend.utils.DateHelper
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowMovementBottomSheetButton

@Composable
fun MovementCard(
    movementWithCategory: MovementWithCategory,
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    summaryViewModel: SummaryViewModel,
    clickableButton: Boolean = true
) {
    val colorFilter: ColorFilter
    val background: Color
    val icon: ImageVector

    if (movementWithCategory.movement.amount > 0) {
        colorFilter = ColorFilter.tint(colorResource(id = R.color.green_700))
        background = colorResource(id = R.color.green_100)
        icon = ImageVector.vectorResource(id = R.drawable.baseline_trending_up_24)
    } else {
        colorFilter = ColorFilter.tint(colorResource(id = R.color.red_700))
        background = colorResource(id = R.color.red_100)
        icon = ImageVector.vectorResource(id = R.drawable.baseline_trending_down_24)
    }
    ListItem(
        headlineContent = {
            Text(
                DateHelper.convertFromMillisecondsToDateTime(movementWithCategory.movement.date)
            )
        },
        supportingContent = { Text(movementWithCategory.category.identifier) },
        leadingContent = {
            Image(
                painter = rememberVectorPainter(icon),
                colorFilter = colorFilter,
                contentDescription = "Stonks or Not stonks",
                modifier = Modifier
                    .background(
                        background,
                        CircleShape
                    )
                    .padding(10.dp)
            )
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(
                        id = R.string.amount,
                        movementWithCategory.movement.amount,
                        Constants.CURRENCY
                    ),
                    style = MaterialTheme.typography.headlineSmall,
                )

                if(clickableButton) {
                    ShowMovementBottomSheetButton(
                        movementWithCategory,
                        categoryViewModel,
                        movementWithCategoryViewModel,
                        summaryViewModel
                    )
                }
            }
        }
    )
}