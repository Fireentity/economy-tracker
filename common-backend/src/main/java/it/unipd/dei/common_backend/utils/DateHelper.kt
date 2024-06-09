package it.unipd.dei.common_backend.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateHelper {
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

    fun selectDateTime(context: Context, callback: (String) -> Unit) {
        showDateTimePickerDialog(context, callback)
    }

    private fun showDateTimePickerDialog(context: Context, callback: (String) -> Unit) {
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