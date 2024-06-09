package it.unipd.dei.jetpack_compose_frontend.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.ui.bottomsheets.CategoryBottomSheet
import it.unipd.dei.jetpack_compose_frontend.ui.cards.CategoryCard


@Composable
fun CategoriesScreen(
    categoryViewModel: CategoryViewModel,
) {
    Surface {
        Scaffold { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                val categories: List<String> = categoryViewModel
                    .allCategories
                    .value
                    ?.keys
                    ?.toList() ?: emptyList()


                LazyColumn {
                    items(categories.size) { index ->
                        var showBottomSheet by remember { mutableStateOf(false) }
                        val category = categoryViewModel.getCategoryByIdentifier(categories[index])
                        category?.let {
                            CategoryCard(it) {
                                showBottomSheet = true
                            }
                        }

                        if (showBottomSheet) {
                            category?.let {
                                CategoryBottomSheet(
                                    category = category,
                                    categoryViewModel = categoryViewModel
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}