package it.unipd.dei.xml_frontend.ui.input

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher

import com.google.android.material.textfield.TextInputEditText
import it.unipd.dei.common_backend.models.Movement

class MovementAmountInput(
    view: TextInputEditText,
    movement: Movement? = null
) {
    init {
        val decimalDigitsInputFilter = InputFilter { source, _, _, dest, _, _ ->
            if ((dest.toString() + source.toString()).matches(Regex("^(-)?\\d{0,6}(\\.\\d{0,2})?$"))) null else ""
        }
        view.filters = arrayOf(decimalDigitsInputFilter)

        view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                editable?.let {
                    val text = it.toString()
                    if (text.isNotEmpty() && text != "-" && !text.endsWith(".")) {
                        val decimalIndex = text.indexOf(".")
                        if (decimalIndex != -1 && text.length - decimalIndex > 3) {
                            it.replace(decimalIndex + 3, text.length, "")
                        }
                    }
                }
            }
        })
        if(movement!=null){
            view.setText(movement.amount.toString())
        }
    }

    private var amount = movement?.amount

    fun getAmount(): Double? = amount

    fun onTextChanged(text: CharSequence?) {
        this.amount = text.toString().toDoubleOrNull()
    }

}