package it.unipd.dei.jetpack_compose_frontend.ui.tabs

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import it.unipd.dei.common_backend.view.CategoryViewModel
import it.unipd.dei.common_backend.view.MovementWithCategoryViewModel
import it.unipd.dei.jetpack_compose_frontend.ui.cards.MovementCard


@Composable
fun AllTab(movementWithCategoryViewModel: MovementWithCategoryViewModel, categoryViewModel: CategoryViewModel) {
    movementWithCategoryViewModel.loadInitialMovementsByCategory()
    val movements by movementWithCategoryViewModel.getMovements()
        .observeAsState(initial = emptyList())

    val listState = rememberLazyListState()
    val reachedBottom: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index != 0 && lastVisibleItem?.index == listState.layoutInfo.totalItemsCount - 1
        }
    }
    LaunchedEffect(reachedBottom) {
        if (reachedBottom) movementWithCategoryViewModel.loadSomeMovementsByCategory { }
    }
    LazyColumn(state = listState) {
        items(movements.size) { index ->
            MovementCard(
                movements[index],
                categoryViewModel,
                movementWithCategoryViewModel
            )
        }
    }
}