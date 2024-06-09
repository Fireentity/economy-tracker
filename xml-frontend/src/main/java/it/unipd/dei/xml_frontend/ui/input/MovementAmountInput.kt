package it.unipd.dei.xml_frontend.ui.input

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher

import com.google.android.material.textfield.TextInputEditText
import it.unipd.dei.common_backend.models.Movement
import java.util.Locale

class MovementAmountInput(
    view: TextInputEditText,
    movement: Movement? = null
) {
    companion object{
        private val regex = Regex("^(-)?\\d{0,6}(\\.\\d{0,2})?$")
    }

    private var amount = String.format(Locale.US, "%.2f", movement?.amount)


    init {
        val decimalDigitsInputFilter = InputFilter { source, _, _, dest, _, _ ->
            val inputText = dest.toString() + source.toString()
            if (inputText.matches(regex) && !inputText.startsWith(".")) null else ""
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
                onTextChanged(editable)
            }
        })
        if(movement!=null){
            view.setText(amount)
        }
    }

    fun getAmount(): String = amount

    private fun onTextChanged(text: CharSequence?) {
        this.amount = text.toString()
    }

}