package it.unipd.dei.jetpack_compose_frontend.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import it.unipd.dei.common_backend.viewModels.SummaryViewModel
import it.unipd.dei.jetpack_compose_frontend.ui.cards.SummaryCard

@Composable
fun HomeScreen(summaryViewModel: SummaryViewModel) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        summaryViewModel.loadAllSummaryCards()
        val summaries by summaryViewModel.allSummary.observeAsState(initial = listOf())
        summaryViewModel.loadAllSummaryCards()

        LazyColumn {
            items(summaries.size) { index ->
                SummaryCard(
                    summaries[index]
                )
            }
        }
    }
}