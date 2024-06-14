package it.unipd.dei.common_backend.models

import it.unipd.dei.common_backend.utils.DateHelper
import java.time.Month

data class Summary(
    val monthlyAll: Double,
    val monthlyPositive: Double,
    val monthlyNegative: Double,
    val month: Month,
    val year: Int
) {
    companion object {
        val DEFAULT = Summary(
            0.0,
            0.0,
            0.0,
            DateHelper.getCurrentMonth(),
            DateHelper.getCurrentYear()
        )
    }
}