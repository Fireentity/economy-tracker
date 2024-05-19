package it.unipd.dei.music_application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class CashflowViewModel(
    private val dao: CashflowDao
): ViewModel() {

    private val _sortType = MutableStateFlow(SortType.AMOUNT)
    private val _cashflows = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.AMOUNT -> dao.getAllCashflowOrderedById()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(CashflowState())
    val state = combine(_state, _sortType, _cashflows) { state, sortType, cashflows ->
        state.copy(
            cashflows = cashflows,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CashflowState())

    fun onEvent(event: CashflowEvent) {
        when(event) {
            is CashflowEvent.DeleteCashflow -> {
                viewModelScope.launch {
                    dao.deleteMoneyMovement(event.cashflow)
                }
            }
            CashflowEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingCashflow = false
                ) }
            }
            CashflowEvent.SaveCashflow -> {
                val amount = state.value.amount

                if(amount.equals(0.0)) {
                    return
                }

                val cashflow = Cashflow(
                    amount = amount
                )
                viewModelScope.launch {
                    dao.insertMoneyMovement(cashflow)
                }
                _state.update { it.copy(
                    isAddingCashflow = false,
                    amount = 0.0
                ) }
            }
            is CashflowEvent.SetAmount -> {
                _state.update { it.copy(
                    amount = event.amount
                ) }
            }
            CashflowEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingCashflow = true
                ) }
            }
            is CashflowEvent.SortCashflows -> {
                _sortType.value = event.sortType
            }
        }
    }
}