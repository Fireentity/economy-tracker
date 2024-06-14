package it.unipd.dei.common_backend.utils

import android.text.format.DateFormat
import it.unipd.dei.common_backend.converters.MonthTypeConverter
import it.unipd.dei.common_backend.models.MonthInfo
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.Month
import java.util.Locale

object DateHelper {

    fun isMonthNotFinishedYet(month: Month, year: Int): Boolean {
        return getMonthEndInMilliseconds(month, year) > System.currentTimeMillis()
    }

    fun getMonthEndInMilliseconds(month: Month, year: Int): Long {
        val endOfMonth = LocalDateTime.of(year, month, 1, 23, 59, 59)
            .with(TemporalAdjusters.lastDayOfMonth())
            .withNano(999_999_999)
        return endOfMonth.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun getMonthStartInMilliseconds(month: Month, year: Int): Long {
        val startOfMonth = LocalDateTime.of(year, month, 1, 0, 0, 0)
            .withNano(0)
        return startOfMonth.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun getMonthlyIntervals(startMillis: Long, endMillis: Long): List<MonthInfo> {
        val startDate = LocalDateTime.ofEpochSecond(startMillis / 1000, 0, ZoneOffset.UTC)
        val endDate = LocalDateTime.ofEpochSecond(endMillis / 1000, 0, ZoneOffset.UTC)

        val monthlyIntervals = mutableListOf<MonthInfo>()
        var currentStart = startDate.withDayOfMonth(1)
        var currentEnd = currentStart.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999_999_999)

        while (currentStart.isBefore(endDate)) {
            val startMillis = currentStart.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endMillis = currentEnd.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            monthlyIntervals.add(MonthInfo(startMillis, endMillis, currentStart.month, currentStart.year))

            currentStart = currentStart.plus(1, ChronoUnit.MONTHS).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0)
            currentEnd = currentStart.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59).withNano(999_999_999)
        }

        return monthlyIntervals.apply {
            reverse()
        }
    }

    fun convertFromDateTimeToMilliseconds(dateTime: String): Long? {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault())
        return try {
            val date = LocalDateTime.parse(dateTime, formatter)
            date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        } catch (e: Exception) {
            null
        }
    }

    fun convertFromMillisecondsToDateTime(milliseconds: Long): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault())
        return LocalDateTime.ofEpochSecond(milliseconds / 1000, 0, ZoneOffset.UTC)
            .atZone(ZoneId.systemDefault())
            .format(formatter)
    }

    fun getCurrentMonth(): Month {
        return LocalDateTime.now().month
    }

    fun getCurrentYear(): Int {
        return LocalDateTime.now().year
    }

    fun getCurrentMonthInfo(): MonthInfo {
        val month = getCurrentMonth()
        val year = getCurrentYear()
        return MonthInfo(
            getMonthStartInMilliseconds(month, year),
            getMonthEndInMilliseconds(month, year),
            month,
            year
        )
    }
}
