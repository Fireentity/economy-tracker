package it.unipd.dei.common_backend.models

import java.time.Month

data class MonthInfo(
    val startDate: Long,
    val endDate: Long,
    val month: Month,
    val year: Int
)
