package it.unipd.dei.xml_frontend.ui.view.holder

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.widget.TextView
import androidx.core.graphics.toColor
import com.google.android.material.chip.Chip
import it.unipd.dei.common_backend.models.SummaryCard
import it.unipd.dei.common_backend.utils.Constants
import it.unipd.dei.common_backend.utils.DateHelper
import it.unipd.dei.common_backend.utils.TextGenerator
import it.unipd.dei.xml_frontend.R
import java.time.Month

class SummaryViewHolder(
    private val itemView: View,
    private val context: Context
) {
    private val allChip: Chip = itemView.findViewById(R.id.all_chip_summary_card)
    private val revenueChip: Chip = itemView.findViewById(R.id.revenue_chip_summary_card)
    private val expensesChip: Chip = itemView.findViewById(R.id.expense_chip_summary_card)
    private val titleTextView: TextView = itemView.findViewById(R.id.title_summary_card)
    private val subTitleTextView: TextView = itemView.findViewById(R.id.sub_title_summary_card)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.description_summary_card)
    fun getItemView(): View = itemView

    fun bind(summaryCard: SummaryCard) {

        allChip.apply {
            if (summaryCard.monthlyAll > 0) {
                setTextColor(context.getColor(R.color.green_700))
                chipBackgroundColor = ColorStateList.valueOf(context.getColor(R.color.green_100))
            } else if (summaryCard.monthlyAll < 0) {
                setTextColor(context.getColor(R.color.red_700))
                chipBackgroundColor = ColorStateList.valueOf(context.getColor(R.color.red_100))
            }
            text = String.format("%.2f", summaryCard.monthlyAll)
        }
        revenueChip.text = String.format("%.2f", summaryCard.monthlyPositive)
        expensesChip.text = String.format("%.2f", summaryCard.monthlyNegative)

        val monthOfYear = Constants.monthOf(summaryCard.month) + " " + summaryCard.year.toString()
        titleTextView.text = monthOfYear

        subTitleTextView.text =
            if (DateHelper.getMonthEndInMilliseconds(
                    summaryCard.month,
                    summaryCard.year
                ) > System.currentTimeMillis()
            ) {
                context.getString(R.string.how_is_it_going)
            } else {
                context.getString(R.string.how_did_it_go)
            }
        descriptionTextView.text = TextGenerator.generateText(summaryCard)
    }


}