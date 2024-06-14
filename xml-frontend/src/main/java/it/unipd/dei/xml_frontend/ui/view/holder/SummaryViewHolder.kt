package it.unipd.dei.xml_frontend.ui.view.holder

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import com.google.android.material.chip.Chip
import it.unipd.dei.common_backend.models.Summary
import it.unipd.dei.common_backend.utils.Constants
import it.unipd.dei.common_backend.viewholder.ISummaryViewHolder
import it.unipd.dei.xml_frontend.R

class SummaryViewHolder(
    private val itemView: View,
    private val context: Context
) : ISummaryViewHolder {
    private val allChip: Chip = itemView.findViewById(R.id.all_chip_summary_card)
    private val revenueChip: Chip = itemView.findViewById(R.id.revenue_chip_summary_card)
    private val expensesChip: Chip = itemView.findViewById(R.id.expense_chip_summary_card)
    private val titleTextView: TextView = itemView.findViewById(R.id.title_summary_card)
    private val subTitleTextView: TextView = itemView.findViewById(R.id.sub_title_summary_card)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.description_summary_card)
    fun getItemView(): View = itemView

    fun bind(summary: Summary) {

        allChip.text = context.getString(
            R.string.monthly_total,
            summary.monthlyAll,
            Constants.CURRENCY.getSymbol(context.resources)
        )

        revenueChip.text = context.getString(
            R.string.monthly_revenues,
            summary.monthlyPositive,
            Constants.CURRENCY.getSymbol(context.resources)
        )
        expensesChip.text = context.getString(
            R.string.monthly_expenses,
            summary.monthlyNegative,
            Constants.CURRENCY.getSymbol(context.resources)
        )

        val monthOfYear = this.readableDate(summary, context)
        titleTextView.text = monthOfYear

        subTitleTextView.text = headline(summary, context)
        descriptionTextView.text = summaryDescription(summary, context)
    }
}