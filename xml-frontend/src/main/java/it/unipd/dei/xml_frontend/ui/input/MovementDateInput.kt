package it.unipd.dei.xml_frontend.ui.input

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.timepicker.MaterialTimePicker
import it.unipd.dei.common_backend.models.Movement
import it.unipd.dei.common_backend.utils.DateHelper

class MovementDateInput(
    view: TextInputEditText,
    fragmentManager: FragmentManager,
    movement: Movement? = null
) {
    companion object {
        private const val MILLISECONDS_PER_HOUR = 60 * 60 * 1000;
        private const val MILLISECONDS_PER_MINUTE = 60 * 1000;
    }

    private var date = movement?.date

    init {
        view.setOnClickListener {
            selectDateTime(fragmentManager) {
                view.setText(DateHelper.convertFromMillisecondsToDateTime(it))
                date = it
            }
        }

        movement?.date?.let {
            view.setText(DateHelper.convertFromMillisecondsToDateTime(it))
        }
    }

    fun getDate(): Long? = date

    private fun selectDateTime(
        fragmentManager: FragmentManager,
        onDismiss: () -> Unit = {},
        callback: (Long) -> Unit
    ) {
        showDateTimePickerDialog(callback, fragmentManager, onDismiss)
    }

    private fun showDateTimePickerDialog(
        callback: (Long) -> Unit,
        fragmentManager: FragmentManager,
        onDismiss: () -> Unit = {}
    ) {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            //TODO check here
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            showTimePickerDialog(fragmentManager) {
                callback(it + selection)
            }
        }

        datePicker.addOnDismissListener {
            onDismiss()
        }

        datePicker.show(
            fragmentManager,
            //TODO check here
            "MATERIAL_DATE_PICKER"
        )
    }

    private fun showTimePickerDialog(
        fragmentManager: FragmentManager,
        callback: (Long) -> Unit
    ) {
        val timePicker = MaterialTimePicker.Builder()
            //TODO check here
            .setTitleText("Select Time")
            .build()

        timePicker.addOnPositiveButtonClickListener {
            callback((timePicker.hour * MILLISECONDS_PER_HOUR + timePicker.minute * MILLISECONDS_PER_MINUTE).toLong())
        }

        timePicker.show(fragmentManager,
            //TODO check here
            "MATERIAL_TIME_PICKER")
    }
}