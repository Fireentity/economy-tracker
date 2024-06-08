package it.unipd.dei.jetpack_compose_frontend.ui.cards

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun CategoryCard(category: Category) {
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (image, text) = createRefs()

        Icon(
            painter = painterResource(id = R.drawable.baseline_stacked_bar_chart_24),
            contentDescription = "Category Image",
            modifier = Modifier
                .constrainAs(image) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .size(40.dp)
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
    }
}