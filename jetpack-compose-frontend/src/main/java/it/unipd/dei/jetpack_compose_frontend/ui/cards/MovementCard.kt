package it.unipd.dei.jetpack_compose_frontend.ui.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import it.unipd.dei.common_backend.models.MovementWithCategory
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.TimeUtils
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowMovementBottomSheetButton
import java.time.format.DateTimeFormatter

@Composable
fun MovementCard(
    movementWithCategory: MovementWithCategory,
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    ConstraintLayout(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        val (image, date, category, amount, showBottomSheetButton) = createRefs()

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
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        )

        Text(
            text = TimeUtils.zonedDateTimeFromMillis(movementWithCategory.movement.createdAt)
                .format(formatter),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.constrainAs(date) {
                start.linkTo(image.end, margin = 20.dp)
                top.linkTo(parent.top)
            }
        )

        Text(
            text = movementWithCategory.category.identifier,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.constrainAs(category) {
                start.linkTo(image.end, margin = 20.dp)
                top.linkTo(date.bottom)
            }
        )

        Text(
            text = movementWithCategory.movement.amount.toString() + stringResource(id = R.string.euro),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.constrainAs(amount) {
                end.linkTo(showBottomSheetButton.start, margin = 5.dp)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        )

        ShowMovementBottomSheetButton(
            modifier = Modifier.constrainAs(showBottomSheetButton) {
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            },
            movement = movementWithCategory,
            categoryViewModel = categoryViewModel,
            movementWithCategoryViewModel = movementWithCategoryViewModel
        )
    }

    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 5.dp),
        thickness = 1.dp,
    )
}