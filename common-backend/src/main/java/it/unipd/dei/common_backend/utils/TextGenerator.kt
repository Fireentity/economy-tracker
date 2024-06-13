package it.unipd.dei.common_backend.utils

import it.unipd.dei.common_backend.models.Summary

object TextGenerator {

    private val positivePhrases = listOf(
        "Ottimo lavoro! Questo mese hai guadagnato ",
        "Fantastico! I tuoi guadagni mensili ammontano a ",
        "Congratulazioni! Hai accumulato ben ",
        "Eccellente! Hai portato a casa ",
        "Super! Il tuo guadagno di questo mese è stato di "
    )

    private val negativePhrases = listOf(
        "Attenzione alle spese! Questo mese hai speso ",
        "I tuoi costi mensili sono stati ",
        "Ricorda di tenere d'occhio le tue spese: ammontano a ",
        "Le tue uscite questo mese sono state di ",
        "Controlla le tue spese, hai speso "
    )

    private val totalPhrases = listOf(
        "Il totale netto per il mese è ",
        "Il saldo mensile complessivo è ",
        "Il risultato finale di questo mese è ",
        "Il bilancio di questo mese è ",
        "Il totale delle tue finanze per questo mese è "
    )

    fun generateText(summary: Summary): String {

        val positivePhrase = positivePhrases.random() + "${String.format("%.2f", summary.monthlyPositive)} €.\n"
        val negativePhrase = negativePhrases.random() + "${String.format("%.2f", summary.monthlyNegative)} €.\n"
        val totalPhrase = totalPhrases.random() + "${String.format("%.2f", summary.monthlyAll)} €.\n"
        
        return positivePhrase + negativePhrase + totalPhrase
    }

}