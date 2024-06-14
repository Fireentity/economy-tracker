package it.unipd.dei.common_backend.utils

import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

object DateHelper {

    fun isMonthNotFinishedYet(month: Month, year: Int): Boolean {
        return getMonthEndInMilliseconds(month, year) > System.currentTimeMillis()
    }

    private fun getMonthEndInMilliseconds(month: Month, year: Int): Long {
        val endOfMonth = LocalDateTime.of(year, month, 1, 23, 59, 59)
            .with(TemporalAdjusters.lastDayOfMonth())
            .withNano(999_999_999)
        return endOfMonth.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
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
}
