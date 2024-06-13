package it.unipd.dei.common_backend.models

import java.time.Month

data class Summary(
    val monthlyAll: Double,
    val monthlyPositive: Double,
    val monthlyNegative: Double,
    val month: Month,
    val year: Int
) {

}