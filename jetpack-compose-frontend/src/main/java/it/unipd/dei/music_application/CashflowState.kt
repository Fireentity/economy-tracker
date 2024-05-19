package it.unipd.dei.music_application

data class CashflowState(
    val cashflows: List<Cashflow> = emptyList(),
    val amount: Double = 0.0,
    val isAddingCashflow: Boolean = false,
    val sortType: SortType = SortType.AMOUNT
)
