package it.unipd.dei.common_backend.models

data class SummaryCard(
    val monthlyAll: Double,
    val monthlyPositive: Double,
    val monthlyNegative: Double,
    val month: Int,
    val year: Int
)