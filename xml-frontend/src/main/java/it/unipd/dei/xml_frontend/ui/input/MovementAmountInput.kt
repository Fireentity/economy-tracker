package it.unipd.dei.xml_frontend.ui.input

import it.unipd.dei.common_backend.models.Movement

class MovementAmountInput(
    movement: Movement? = null
) {

    private var amount = movement?.amount

    fun getAmount(): Double? = amount

    fun onTextChanged(text: CharSequence?) {
        this.amount = text.toString().toDoubleOrNull()
    }
}