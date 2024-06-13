package it.unipd.dei.xml_frontend.ui.input

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import it.unipd.dei.common_backend.models.Movement
import it.unipd.dei.common_backend.utils.DateHelper
import java.util.Calendar
import java.util.Locale

class MovementDateInput(
    context: Context,
    view: TextInputEditText,
    fragmentManager: FragmentManager,
    movement: Movement? = null
) {
    private var date = movement?.date?.let { DateHelper.convertFromMillisecondsToDateTime(it) } ?: ""

    init {
        view.setOnClickListener {
            selectDateTime(context, fragmentManager) {
                view.setText(it)
                date = it
            }
        }

        if (movement != null) {
            view.setText(date)
        }
    }

    fun getDate(): String = date

    private fun selectDateTime(
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
                Locale.getDefault(),
                "%02d/%02d/%04d",
                selectedDate.get(Calendar.DAY_OF_MONTH),
                selectedDate.get(Calendar.MONTH),
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
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .setTitleText("Select Time")
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val selectedTime = String.format(
                Locale.getDefault(),
                "%02d:%02d", timePicker.hour, timePicker.minute)
            val selectedDateTime = "$selectedDate $selectedTime"
            callback(selectedDateTime)
        }

        timePicker.show(fragmentManager, "MATERIAL_TIME_PICKER")
    }
}