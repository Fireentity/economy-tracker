package it.unipd.dei.common_backend.viewholder

import android.content.Context
import it.unipd.dei.common_backend.R
import it.unipd.dei.common_backend.models.Summary
import it.unipd.dei.common_backend.utils.Constants
import it.unipd.dei.common_backend.utils.DateHelper
import java.time.Month

interface ISummaryViewHolder {

    fun headline(summary: Summary, context: Context): String {
        return if (DateHelper.isMonthCurrent(summary.month, summary.year)
        ) {
            context.getString(R.string.how_is_it_going)
        } else {
            context.getString(R.string.how_did_it_go)
        }
    }

    fun readableMonth(summary: Summary, context: Context): String {
        val month = summary.month
        val monthName = when (month) {
            Month.JANUARY -> context.resources.getString(R.string.january)
            Month.FEBRUARY -> context.resources.getString(R.string.february)
            Month.MARCH -> context.resources.getString(R.string.march)
            Month.APRIL -> context.resources.getString(R.string.april)
            Month.MAY -> context.resources.getString(R.string.may)
            Month.JUNE -> context.resources.getString(R.string.june)
            Month.JULY -> context.resources.getString(R.string.july)
            Month.AUGUST -> context.resources.getString(R.string.august)
            Month.SEPTEMBER -> context.resources.getString(R.string.september)
            Month.OCTOBER -> context.resources.getString(R.string.october)
            Month.NOVEMBER -> context.resources.getString(R.string.november)
            Month.DECEMBER -> context.resources.getString(R.string.december)
            else -> throw IllegalArgumentException("Invalid month number")
        }
        return monthName
    }

    fun summaryDescription(summary: Summary, context: Context): String {
        val monthlyAll = summary.monthlyAll
        val monthlyPositive = summary.monthlyPositive
        val monthlyNegative = summary.monthlyNegative

        val positivePhrases = listOf(
            context.getString(
                R.string.great_job_this_month_you_earned,
                monthlyPositive,
                Constants.CURRENCY
            ),
            context.getString(
                R.string.fantastic_your_monthly_earnings_are,
                monthlyPositive,
                Constants.CURRENCY
            ),
            context.getString(
                R.string.congratulations_you_have_accumulated,
                monthlyPositive,
                Constants.CURRENCY
            ),
            context.getString(
                R.string.excellent_you_have_taken_home,
                monthlyPositive,
                Constants.CURRENCY
            ),
            context.getString(
                R.string.super_your_earnings_this_month_have_been,
                monthlyAll,
                Constants.CURRENCY
            )
        )

        val negativePhrases = listOf(
            context.getString(
                R.string.watch_out_for_expenses_this_month_you_spent,
                monthlyNegative,
                Constants.CURRENCY
            ),
            context.getString(
                R.string.your_monthly_costs_have_been,
                monthlyNegative,
                Constants.CURRENCY
            ),
            context.getString(
                R.string.remember_to_watch_your_expenses_they_amount_to,
                monthlyNegative,
                Constants.CURRENCY
            ),
            context.getString(
                R.string.your_expenses_this_month_have_been,
                monthlyNegative,
                Constants.CURRENCY
            ),
            context.getString(
                R.string.control_your_expenses_you_spent,
                monthlyNegative,
                Constants.CURRENCY
            )
        )

        val totalPhrases = listOf(
            context.getString(
                R.string.the_net_total_for_the_month_is,
                monthlyAll,
                Constants.CURRENCY
            ),
            context.getString(
                R.string.the_overall_monthly_balance_is,
                monthlyAll,
                Constants.CURRENCY
            ),
            context.getString(
                R.string.the_final_result_for_this_month_is,
                monthlyAll,
                Constants.CURRENCY
            ),
            context.getString(
                R.string.the_balance_for_this_month_is,
                monthlyAll,
                Constants.CURRENCY
            ),
            context.getString(
                R.string.the_total_for_your_finances_this_month_is,
                monthlyAll,
                Constants.CURRENCY
            )
        )

        val phrases = listOf(
            positivePhrases[summary.month.value % positivePhrases.size],
            negativePhrases[summary.month.value % negativePhrases.size],
            totalPhrases[summary.month.value % totalPhrases.size]
        )


        return phrases.joinToString("\n");
    }

    fun readableDate(summary: Summary, context: Context): String {
        return context.getString(
            R.string.month_date,
            readableMonth(summary, context),
            summary.year.toString()
        )
    }
}