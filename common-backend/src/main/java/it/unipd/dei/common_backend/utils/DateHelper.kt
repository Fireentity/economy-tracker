package it.unipd.dei.common_backend.utils

import android.text.format.DateFormat
import it.unipd.dei.common_backend.models.MonthInfo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Month
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

object DateHelper {

    fun isMonthCurrent(month: Month, year: Int): Boolean {
        return getMonthEndInMilliseconds(
            month,
            year
        ) > System.currentTimeMillis()
    }

    fun getMonthEndInMilliseconds(month: Month, year: Int): Long {
        val calendar = GregorianCalendar(year, month.value, 1, 23, 59, 59)
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }

    fun getMonthlyIntervals(startMillis: Long, endMillis: Long): List<MonthInfo> {
        val startCalendar = GregorianCalendar().apply { timeInMillis = startMillis }
        val endCalendar = GregorianCalendar().apply { timeInMillis = endMillis }

        // Set the start date to the beginning of the month
        startCalendar.set(Calendar.DAY_OF_MONTH, 1)
        // Adjust time to start of the day
        startCalendar.set(Calendar.HOUR_OF_DAY, 0)
        startCalendar.set(Calendar.MINUTE, 0)
        startCalendar.set(Calendar.SECOND, 0)
        startCalendar.set(Calendar.MILLISECOND, 0)

        val monthlyIntervals = mutableListOf<MonthInfo>()

        while (startCalendar.timeInMillis <= endCalendar.timeInMillis) {
            val currentStartMillis = startCalendar.timeInMillis

            // Move to the end of the current month
            startCalendar.set(
                Calendar.DAY_OF_MONTH,
                startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            )
            // Adjust time to end of the day
            startCalendar.set(Calendar.HOUR_OF_DAY, 23)
            startCalendar.set(Calendar.MINUTE, 59)
            startCalendar.set(Calendar.SECOND, 59)
            startCalendar.set(Calendar.MILLISECOND, 999)

            val currentEndMillis = startCalendar.timeInMillis

            monthlyIntervals.add(
                MonthInfo(
                    startDate = currentStartMillis,
                    endDate = currentEndMillis,
                    month = startCalendar.get(Calendar.MONTH),
                    year = startCalendar.get(Calendar.YEAR)
                )
            )

            // Move to the start of the next month
            startCalendar.add(Calendar.DAY_OF_MONTH, 1)
            startCalendar.set(Calendar.DAY_OF_MONTH, 1)
            startCalendar.set(Calendar.HOUR_OF_DAY, 0)
            startCalendar.set(Calendar.MINUTE, 0)
            startCalendar.set(Calendar.SECOND, 0)
            startCalendar.set(Calendar.MILLISECOND, 0)
        }

        return monthlyIntervals.apply {
            reverse()
        }
    }


    fun convertFromDateTimeToMilliseconds(dateTime: String): Long? {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return try {
            val date = dateFormat.parse(dateTime)
            date?.time ?: 0L
        } catch (e: ParseException) {
            return null
        }
    }

    fun convertFromMillisecondsToDateTime(milliseconds: Long): String {
        return DateFormat.format("dd/MM/yyyy hh:mm", milliseconds)
            .toString()
    }
}