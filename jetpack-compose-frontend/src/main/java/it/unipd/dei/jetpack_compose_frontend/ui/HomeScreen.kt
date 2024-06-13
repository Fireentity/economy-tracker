package it.unipd.dei.jetpack_compose_frontend.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.cards.SummaryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(summaryViewModel: SummaryViewModel) {
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
                            stringResource(R.string.home),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            },
        ) { paddingValues ->

            Column(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                summaryViewModel.loadAllSummaries()
                val summaries by summaryViewModel.allSummary.observeAsState(initial = listOf())
                summaryViewModel.loadAllSummaries()

                LazyColumn {
                    items(summaries.size) { index ->
                        SummaryCard(
                            summaries[index]
                        )
                    }
                }
            }
        }
    }
}