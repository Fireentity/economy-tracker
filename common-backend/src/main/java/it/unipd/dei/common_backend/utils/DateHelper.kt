package it.unipd.dei.common_backend.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat
import it.unipd.dei.common_backend.models.MonthInfo
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

object DateHelper {

    fun getMonthStartInMilliseconds(month: Int, year: Int): Long {
        val calendar = GregorianCalendar()

        // Imposta il calendario al primo giorno del mese specificato e all'inizio del giorno
        calendar.set(year, month - 1, 1, 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }

    fun getMonthEndInMilliseconds(month: Int, year: Int): Long {
        val calendar = GregorianCalendar(year, month - 1, 1, 23, 59, 59)
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
                    month = startCalendar.get(Calendar.MONTH) + 1,
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

    fun selectDateTime(context: Context, onDismiss: () -> Unit = {}, callback: (String) -> Unit) {
        showDateTimePickerDialog(context, callback, onDismiss)
    }

    private fun showDateTimePickerDialog(
        context: Context,
        callback: (String) -> Unit,
        onDismiss: () -> Unit = {}
    ) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = String.format(
                    "%02d/%02d/%04d",
                    selectedDayOfMonth,
                    selectedMonth + 1,
                    selectedYear
                )
                showTimePickerDialog(selectedDate, context, callback)
            },
            year,
            month,
            dayOfMonth
        )

        datePickerDialog.setOnDismissListener {
            onDismiss()
        }
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(
        selectedDate: String,
        context: Context,
        callback: (String) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            context,
            { _, selectedHourOfDay, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHourOfDay, selectedMinute)
                val selectedDateTime = "$selectedDate $selectedTime"
                callback(selectedDateTime)
            },
            hourOfDay,
            minute,
            true
        )

        timePickerDialog.show()
    }

}