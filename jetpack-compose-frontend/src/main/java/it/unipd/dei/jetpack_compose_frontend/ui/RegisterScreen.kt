package it.unipd.dei.jetpack_compose_frontend.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.ui.buttons.ShowAddMovementDialogButton
import it.unipd.dei.jetpack_compose_frontend.ui.cards.MovementCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel
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
                    //scrollBehavior = scrollBehavior,
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

                movementWithCategoryViewModel.loadInitialMovementsByCategory()
                val movements by movementWithCategoryViewModel.getMovements()
                    .observeAsState(initial = emptyList())
                val negativeMovements by movementWithCategoryViewModel.getNegativeMovement()
                    .observeAsState(initial = emptyList())
                val positiveMovements by movementWithCategoryViewModel.getPositiveMovement()
                    .observeAsState(initial = emptyList())
                val listState = rememberLazyListState()
                val reachedBottom: Boolean by remember {
                    derivedStateOf {
                        val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                        lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
                    }
                }
                LaunchedEffect(reachedBottom) {
                    if (reachedBottom) movementWithCategoryViewModel.loadSomeMovementsByCategory {  }
                }
                when (selectedTabIndex) {
                    0 -> LazyColumn(state = listState) {
                        items(positiveMovements.size) { index ->
                            MovementCard(
                                positiveMovements[index],
                                categoryViewModel,
                                movementWithCategoryViewModel
                            )
                        }
                    }

                    1 -> LazyColumn {
                        items(movements.size) { index ->
                            MovementCard(
                                movements[index],
                                categoryViewModel,
                                movementWithCategoryViewModel
                            )
                        }
                    }

                    2 -> LazyColumn {
                        items(negativeMovements.size) { index ->
                            MovementCard(
                                negativeMovements[index],
                                categoryViewModel,
                                movementWithCategoryViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}