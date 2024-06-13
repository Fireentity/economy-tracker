package it.unipd.dei.common_backend.utils

object Constants {
    const val CURRENCY = "€"

    fun monthOf(month: Int): String {
        val monthName = when (month) {
            1 -> "Gennaio"
            2 -> "Febbraio"
            3 -> "Marzo"
            4 -> "Aprile"
            5 -> "Maggio"
            6 -> "Giugno"
            7 -> "Luglio"
            8 -> "Agosto"
            9 -> "Settembre"
            10 -> "Ottobre"
            11 -> "Novembre"
            12 -> "Dicembre"
            else -> ""
        }
        return monthName
    }
}