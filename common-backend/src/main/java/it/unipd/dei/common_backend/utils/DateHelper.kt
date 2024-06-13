package it.unipd.dei.common_backend.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
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

    fun selectDateTime(
        context: Context,
        fragmentManager: FragmentManager,
        onDismiss: () -> Unit = {},
        callback: (String) -> Unit
    ) {
        showDateTimePickerDialog(context, callback, fragmentManager, onDismiss)
    }

    private fun showDateTimePickerDialog(
        context: Context,
        callback: (String) -> Unit,
        fragmentManager: FragmentManager,
        onDismiss: () -> Unit = {}
    ) {
        val calendar = Calendar.getInstance()
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = calendar.apply {
                timeInMillis = selection
            }
            val formattedDate = String.format(
                "%02d/%02d/%04d",
                selectedDate.get(Calendar.DAY_OF_MONTH),
                selectedDate.get(Calendar.MONTH) + 1,
                selectedDate.get(Calendar.YEAR)
            )
            showTimePickerDialog(formattedDate, context, fragmentManager, callback)
        }

        datePicker.addOnDismissListener {
            onDismiss()
        }

        datePicker.show(fragmentManager, "MATERIAL_DATE_PICKER")
    }

    private fun showTimePickerDialog(
        selectedDate: String,
        context: Context,
        fragmentManager: FragmentManager,
        callback: (String) -> Unit
    ) {
        val calendar = Calendar.getInstance()
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setTitleText("Select Time")
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val selectedTime = String.format("%02d:%02d", timePicker.hour, timePicker.minute)
            val selectedDateTime = "$selectedDate $selectedTime"
            callback(selectedDateTime)
        }

        timePicker.show(fragmentManager, "MATERIAL_TIME_PICKER")
    }
}