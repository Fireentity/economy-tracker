package it.unipd.dei.jetpack_compose_frontend.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.AddCategoryButton
import java.util.UUID

@Composable
fun UpsertCategoryDialog(
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    title: String,
    onDismiss: () -> Unit = {},
    category: Category? = null
) {
    var categoryIdentifier by remember { mutableStateOf(category?.identifier ?: "") }
    val modifiedCategory: Category by remember {
        derivedStateOf {
            category?.withCategoryIdentifier(categoryIdentifier)?:Category(
                UUID.randomUUID(),
                categoryIdentifier,
                System.currentTimeMillis(),
                System.currentTimeMillis()
            )
        }
    }
    val error: Boolean by remember {
        derivedStateOf {
            categoryViewModel.getCategoryByIdentifier(categoryIdentifier) != null
        }
    }

    Dialog(
        onDismissRequest = { onDismiss() },
        content = {
            Surface(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        label = { Text(text = stringResource(R.string.category)) },
                        value = categoryIdentifier,
                        onValueChange = { categoryIdentifier = it },
                        modifier = Modifier.fillMaxWidth(),
                        isError = error
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { onDismiss() }) {
                            Text(text = stringResource(R.string.cancel))
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        AddCategoryButton(
                            modifiedCategory,
                            onDismiss,
                            onDismiss,
                            categoryViewModel,
                            movementWithCategoryViewModel
                        )
                    }
                }
            }
        }
    )
}