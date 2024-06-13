package it.unipd.dei.jetpack_compose_frontend.ui.cards

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import it.unipd.dei.jetpack_compose_frontend.R

@Composable
fun SummaryCard() {
    val arrowUpward = Icons.Filled.KeyboardArrowUp
    val arrowDownward = Icons.Filled.KeyboardArrowUp
    val stackedBarChart = Icons.Filled.KeyboardArrowUp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Or use CardDefaults.elevatedCardElevation()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Aprile 2024",
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                modifier = Modifier.padding(top = 8.dp)
            ) {

                ElevatedAssistChip(
                    onClick = { /* Do nothing, as chip is not clickable */ },
                    label = { Text("+200.000$") },
                    leadingIcon = {
                        Icon(
                            imageVector = arrowUpward,
                            contentDescription = "Positive",
                            tint = Color(0xFF15803D)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors().copy(containerColor = colorResource(
                        id = R.color.green_100
                    )),
                    modifier = Modifier.padding(end = 5.dp)
                )
                ElevatedAssistChip(
                    onClick = { /* Do nothing, as chip is not clickable */ },
                    label = { Text("-200.000$") },
                    leadingIcon = {
                        Icon(
                            imageVector = arrowUpward,
                            contentDescription = "Negative",
                            tint = Color(0xFFB91C1C)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors().copy(containerColor = colorResource(
                        id = R.color.green_100
                    )),
                    modifier = Modifier.padding(horizontal = 5.dp)
                )
                ElevatedAssistChip(
                    onClick = { /* Do nothing, as chip is not clickable */ },
                    label = { Text("-200.000$") },
                    leadingIcon = {
                        Icon(
                            imageVector = arrowUpward,
                            contentDescription = "Chart",
                            tint = Color(0xFFB91C1C)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors().copy(containerColor = colorResource(
                        id = R.color.green_100
                    )),
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
            Text(
                text = "Lorem ipsum",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur tempor efficitur arcu et sagittis. Nulla vel est vel tortor varius varius. Vestibulum sollicitudin, tellus vel tincidun",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}