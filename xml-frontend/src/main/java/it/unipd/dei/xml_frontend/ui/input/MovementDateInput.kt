package it.unipd.dei.xml_frontend.ui.input

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.google.android.material.textfield.TextInputEditText
import it.unipd.dei.common_backend.models.Movement
import it.unipd.dei.common_backend.utils.DateHelper

class MovementDateInput(
    context: Context,
    view: TextInputEditText,
    fragmentManager: FragmentManager,
    movement: Movement? = null
) {
    private var date = movement?.date?.let { DateHelper.convertFromMillisecondsToDateTime(it) } ?: ""

    init {
        view.setOnClickListener {
            DateHelper.selectDateTime(context, fragmentManager) {
                view.setText(it)
                date = it
            }
        }

        if (movement != null) {
            view.setText(date)
        }
    }

    fun getDate(): String = date
}