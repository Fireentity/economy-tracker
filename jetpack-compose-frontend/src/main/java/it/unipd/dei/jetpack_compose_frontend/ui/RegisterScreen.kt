package it.unipd.dei.jetpack_compose_frontend.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import it.unipd.dei.common_backend.models.Summary
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowAddMovementDialogButton
import it.unipd.dei.jetpack_compose_frontend.ui.input.CategoryFilterInput
import it.unipd.dei.jetpack_compose_frontend.ui.tabs.AllTab
import it.unipd.dei.jetpack_compose_frontend.ui.tabs.ExpensesTab
import it.unipd.dei.jetpack_compose_frontend.ui.tabs.RevenuesTab


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    summary: Summary
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
                            "Registro",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        CategoryFilterInput(
                            movementWithCategoryViewModel = movementWithCategoryViewModel,
                            categoryViewModel = categoryViewModel
                        )
                    }
                )
            },
            floatingActionButton = {
                ShowAddMovementDialogButton(categoryViewModel, movementWithCategoryViewModel)
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                var selectedTabIndex by rememberSaveable {
                    mutableIntStateOf(1)
                }

                TabRow(
                    selectedTabIndex = selectedTabIndex
                ) {

                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = {
                            selectedTabIndex = 0

                        },
                        text = { Text(text = "Entrate") },
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = {
                            selectedTabIndex = 1

                        },
                        text = { Text(text = "Tutti") },
                    )
                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = {
                            selectedTabIndex = 2

                        },
                        text = { Text(text = "Uscite") },
                    )
                }

                when (selectedTabIndex) {
                    0 -> RevenuesTab(
                        movementWithCategoryViewModel = movementWithCategoryViewModel,
                        categoryViewModel = categoryViewModel,
                        summary
                    )

                    1 -> AllTab(
                        movementWithCategoryViewModel = movementWithCategoryViewModel,
                        categoryViewModel = categoryViewModel,
                        summary

                    )

                    2 -> ExpensesTab(
                        movementWithCategoryViewModel = movementWithCategoryViewModel,
                        categoryViewModel = categoryViewModel,
                        summary

                    )
                }
            }
        }
    }
}