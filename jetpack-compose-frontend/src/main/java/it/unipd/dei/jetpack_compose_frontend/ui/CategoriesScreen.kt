package it.unipd.dei.jetpack_compose_frontend.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowAddCategoryDialogButton
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowDrawerButton
import it.unipd.dei.jetpack_compose_frontend.ui.cards.CategoryCard
import it.unipd.dei.jetpack_compose_frontend.ui.input.CategoriesSearchBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    summaryViewModel: SummaryViewModel,
    drawerState: DrawerState
) {
    Surface {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(
                            stringResource(R.string.categories),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        ShowDrawerButton(drawerState = drawerState)
                    }
                )
            },
            floatingActionButton = {
                ShowAddCategoryDialogButton(
                    categoryViewModel = categoryViewModel,
                    movementWithCategoryViewModel
                )
            }) { paddingValues ->

            Column(
                Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                val categories by categoryViewModel.allCategories
                    .observeAsState(initial = emptyMap())

                val categoriesList by remember {
                    derivedStateOf {
                        categories.keys.toList()
                    }
                }

                CategoriesSearchBar(
                    categoryViewModel,
                    movementWithCategoryViewModel,
                    summaryViewModel
                )

                LazyColumn {
                    items(categories.size) { index ->
                        val category =
                            categoryViewModel.getCategoryByIdentifier(categoriesList[index])
                        category?.let {
                            CategoryCard(
                                it,
                                categoryViewModel,
                                movementWithCategoryViewModel,
                                summaryViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}