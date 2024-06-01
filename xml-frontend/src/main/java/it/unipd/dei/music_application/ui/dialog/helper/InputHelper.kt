package it.unipd.dei.music_application.ui.dialog.helper

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.widget.Button
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class InputHelper {
    companion object {
        fun convertToMilliseconds(dateTime: String): Long {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            return try {
                val date = dateFormat.parse(dateTime)
                date?.time ?: 0L
            } catch (e: Exception) {
                -1
            }
        }

        fun showTemporaryError(textField: TextView, button: Button) {
            val previousTextColor = textField.textColors
            val previousButtonTextColor = button.textColors
            textField.setTextColor(Color.RED)
            button.isEnabled = false
            button.setTextColor(Color.RED)
            CoroutineScope(Dispatchers.Main).launch {
                delay(1500)
                textField.setTextColor(previousTextColor)
                button.setTextColor(previousButtonTextColor)
                button.isEnabled = true
            }
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

        private fun showTimePickerDialog(selectedDate: String, context: Context, callback: (String) -> Unit) {
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
}