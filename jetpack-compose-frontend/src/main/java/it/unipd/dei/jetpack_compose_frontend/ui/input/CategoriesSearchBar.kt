package it.unipd.dei.jetpack_compose_frontend.ui.input

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import it.unipd.dei.common_backend.viewModels.MovementWithCategoryViewModel
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.cards.CategoryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesSearchBar(
    categoryViewModel: CategoryViewModel,
    movementWithCategoryViewModel: MovementWithCategoryViewModel,
    summaryViewModel: SummaryViewModel
) {
    var text by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val categories by categoryViewModel.allCategories
        .observeAsState(initial = emptyMap())

    val filteredCategories by remember {
        derivedStateOf {
            categories.values.filter { it.identifier.contains(text) }
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentWidth()
    ) {
        SearchBar(
            leadingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    if (expanded) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    } else {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = stringResource(id = R.string.search_category_placeholder))
                    }
                }
            },
            placeholder = { Text(text = stringResource(id = R.string.search_category_placeholder)) },
            active = expanded,
            onActiveChange = { expanded = it },
            onQueryChange = {
                text = it
            },
            onSearch = {
                text = it
            },
            query = text,
            modifier = Modifier
                .align(Alignment.TopCenter)
        ) {
            LazyColumn {
                items(filteredCategories.size) {
                    CategoryCard(
                        category = filteredCategories[it],
                        categoryViewModel = categoryViewModel,
                        movementWithCategoryViewModel,
                        summaryViewModel
                    )
                }
            }
        }
    }
}