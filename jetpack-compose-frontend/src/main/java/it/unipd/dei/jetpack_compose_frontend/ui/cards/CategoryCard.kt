package it.unipd.dei.jetpack_compose_frontend.ui.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun CategoryCard(category: Category, onClick: () -> Unit) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (image, text, showBottomSheetButton) = createRefs()
        val icon = ImageVector.vectorResource(id = R.drawable.baseline_stacked_bar_chart_24)

        Image(
            painter = rememberVectorPainter(icon),
            contentDescription = "Category card icon",
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .padding(26.dp)
        )

        Text(
            text = category.identifier,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .constrainAs(text) {
                    start.linkTo(image.end, margin = 30.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(vertical = 8.dp)
        )

        IconButton(
            onClick = {
                onClick()
            },
            modifier = Modifier
                .constrainAs(showBottomSheetButton) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .padding(end = 15.dp)
                .padding(24.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "View category"
            )
        }
    }
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 5.dp),
        thickness = 1.dp,
    )
}