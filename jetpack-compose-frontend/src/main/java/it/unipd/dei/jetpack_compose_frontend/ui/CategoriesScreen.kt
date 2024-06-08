package it.unipd.dei.jetpack_compose_frontend.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowAddCategoryDialogButton
import it.unipd.dei.jetpack_compose_frontend.ui.cards.CategoryCard


@Composable
fun CategoriesScreen(
    categoryViewModel: CategoryViewModel = hiltViewModel()
) {
    categoryViewModel.loadAllCategories()
    Surface {
        Scaffold(
            floatingActionButton = {
                ShowAddCategoryDialogButton()
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                val categories: List<String> = categoryViewModel
                    .allCategories
                    .value
                    ?.keys
                    ?.toList()?: emptyList()

                LazyColumn {
                    items(categories.size) { index ->
                        categoryViewModel.getCategoryByIdentifier(categories[index])
                            ?.let { CategoryCard(it) }
                    }
                }
            }
        }
    }
}