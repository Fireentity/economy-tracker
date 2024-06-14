package it.unipd.dei.common_backend.utils

import android.content.SharedPreferences
import it.unipd.dei.common_backend.enums.Currency

object Constants {
    var CURRENCY: Currency = Currency.EURO

    fun setCurrency(preferences: SharedPreferences, currencySharedPreferenceKey: String) {
        val enumKey = preferences.getString(currencySharedPreferenceKey, Currency.EURO.name)?: ""
        CURRENCY = Currency.valueOf(enumKey)
    }
}