package it.unipd.dei.xml_frontend.ui.view.holder

import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.material.chip.Chip
import it.unipd.dei.common_backend.models.SummaryCard
import it.unipd.dei.common_backend.utils.DateHelper
import it.unipd.dei.xml_frontend.R
import java.util.Locale

class SummaryViewHolder(
    private val itemView: View,
    private val context: Context
) {
    private val allChip: Chip = itemView.findViewById(R.id.all_chip_summary_card)
    private val revenueChip: Chip = itemView.findViewById(R.id.revenue_chip_summary_card)
    private val expensesChip: Chip = itemView.findViewById(R.id.expense_chip_summary_card)
    private val titleTextView: TextView = itemView.findViewById(R.id.title_summary_card)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.description_summary_card)
    fun getItemView(): View = itemView

    fun bind(summaryCard: SummaryCard) {
        allChip.text = String.format("%.2f", summaryCard.monthlyAll)
        revenueChip.text = String.format("%.2f", summaryCard.monthlyPositive)
        expensesChip.text = String.format("%.2f", summaryCard.monthlyNegative)

        val pairStartEnd = DateHelper.getMonthInterval(summaryCard.month, summaryCard.year)
        titleTextView.text =
            if (pairStartEnd.first < System.currentTimeMillis() && pairStartEnd.second > System.currentTimeMillis()) {
                context.getString(R.string.how_is_it_going)
            } else {
                context.getString(R.string.how_did_it_go)
            }
        descriptionTextView.text = generateText()
    }

    private fun generateText(): String {
        return ""
        //TODO implementala
    }
}