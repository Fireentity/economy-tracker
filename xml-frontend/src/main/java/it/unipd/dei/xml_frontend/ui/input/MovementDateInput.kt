package it.unipd.dei.xml_frontend.ui.input

import android.content.Context
import com.google.android.material.textfield.TextInputEditText
import it.unipd.dei.common_backend.models.Movement
import it.unipd.dei.common_backend.utils.DateHelper

class MovementDateInput(
    context: Context,
    view: TextInputEditText,
    movement: Movement? = null
) {
    private var date = movement?.date

    init {
        view.setOnClickListener {
            DateHelper.selectDateTime(context) {
                view.setText(it)
                date = DateHelper.convertFromDateTimeToMilliseconds(it)
            }
        }

        if (movement != null) {
            view.setText(DateHelper.convertFromMillisecondsToDateTime(movement.date))
        }
    }

    fun getDate(): Long? = date
}