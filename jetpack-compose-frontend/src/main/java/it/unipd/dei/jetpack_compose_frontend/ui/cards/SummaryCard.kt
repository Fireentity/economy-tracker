package it.unipd.dei.jetpack_compose_frontend.ui.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedAssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import it.unipd.dei.common_backend.models.Summary
import it.unipd.dei.common_backend.utils.Constants
import it.unipd.dei.jetpack_compose_frontend.R
import it.unipd.dei.jetpack_compose_frontend.ui.viewholder.SummaryViewHolder

@Composable
fun SummaryCard(summary: Summary) {
    val arrowUpward = ImageVector.vectorResource(id = R.drawable.baseline_trending_up_24)
    val arrowDownward = ImageVector.vectorResource(id = R.drawable.baseline_trending_down_24)
    val stackedBarChart = ImageVector.vectorResource(id = R.drawable.baseline_bar_chart_24)
    val summaryViewHolder = SummaryViewHolder()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = summaryViewHolder.readableDate(summary, context = LocalContext.current),
                style = MaterialTheme.typography.titleMedium
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
            ) {

                ElevatedAssistChip(
                    onClick = { /* Do nothing, as chip is not clickable */ },
                    label = {
                        Text(
                            stringResource(
                                R.string.monthly_expenses,
                                summary.monthlyNegative,
                                Constants.CURRENCY.getSymbol(LocalContext.current.resources)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },                    leadingIcon = {
                        Icon(
                            imageVector = arrowDownward,
                            contentDescription = "Monthly expenses",
                            tint = colorResource(id = R.color.red_700)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors().copy(
                        labelColor = colorResource(
                            id = R.color.red_700
                        ),
                        containerColor = colorResource(
                            id = R.color.red_100
                        )
                    )
                )
                ElevatedAssistChip(
                    onClick = { /* Do nothing, as chip is not clickable */ },
                    label = {
                        Text(
                            stringResource(
                                R.string.monthly_total,
                                summary.monthlyAll,
                                Constants.CURRENCY.getSymbol(LocalContext.current.resources)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = stackedBarChart,
                            contentDescription = "Monthly all",
                            tint = colorResource(id = R.color.gray_700)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors().copy(
                        labelColor = colorResource(
                            id = R.color.gray_700
                        ),
                        containerColor = colorResource(
                            id = R.color.gray_100
                        )
                    ),
                )
                ElevatedAssistChip(
                    onClick = { /* Do nothing, as chip is not clickable */ },
                    label = {
                        Text(
                            stringResource(
                                R.string.monthly_revenues,
                                summary.monthlyPositive,
                                Constants.CURRENCY.getSymbol(LocalContext.current.resources)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = arrowUpward,
                            contentDescription = "Monthly revenues",
                            tint = colorResource(id = R.color.green_700)
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors().copy(
                        labelColor = colorResource(
                            id = R.color.green_700
                        ),
                        containerColor = colorResource(
                            id = R.color.green_100
                        )
                    )
                )
            }
            Text(
                text = summaryViewHolder.headline(summary, LocalContext.current),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = summaryViewHolder.summaryDescription(summary, LocalContext.current),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}