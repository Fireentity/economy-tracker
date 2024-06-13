package it.unipd.dei.common_backend.utils

//TODO bisogna usare il file strings.xml altrimenti non è traducibile. Questo è sbagliato
object Constants {
    const val IDENTIFIER_ALREADY_PRESENT_ERROR_MESSAGE = "Identificativo già presente"
    const val OPERATION_SUCCESSFUL_MESSAGE = "Operazione completata con successo."
    const val OPERATION_UNSUCCESSFUL_MESSAGE = "Operazione fallita. Si prega di riprovare.."
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