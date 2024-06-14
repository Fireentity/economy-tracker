package it.unipd.dei.common_backend.enums

import android.content.res.Resources
import it.unipd.dei.common_backend.R

enum class Currency(val value: Int,private val resource: Int) {
    EURO(0, R.string.euro_symbol),
    DOLLAR(1, R.string.dollar_symbol),
    POUND(2, R.string.pound_symbol);


    fun getSymbol(resources: Resources): String {
        return resources.getString(this.resource)
    }

    companion object {
        fun fromInt(value: Int): Currency? {
            return entries.find { it.value == value }
        }
    }
}
