package it.unipd.dei.music_application

sealed interface CashflowEvent {
    object SaveCashflow: CashflowEvent
    data class SetAmount(val amount: Double): CashflowEvent
    object ShowDialog: CashflowEvent
    object HideDialog: CashflowEvent
    data class SortCashflows(val sortType: SortType): CashflowEvent
    data class  DeleteCashflow(val cashflow: Cashflow): CashflowEvent
}