package it.unipd.dei.common_backend.viewModels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipd.dei.common_backend.daos.MovementDao
import it.unipd.dei.common_backend.daos.SummaryCardDao
import it.unipd.dei.common_backend.models.SummaryCard
import it.unipd.dei.common_backend.utils.DateHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SummaryCardViewModel @Inject constructor(
    private val summaryCardDao: SummaryCardDao,
    private val movementDao: MovementDao
) : ViewModel() {
    private val _allSummaryCards = MutableLiveData<List<SummaryCard>>(emptyList())
    val allSummaryCard: LiveData<List<SummaryCard>> = _allSummaryCards


    fun loadAllSummaryCards() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val months = DateHelper.getMonthlyIntervals(
                    movementDao.getLastMovementDate(),
                    movementDao.getFirstMovementDate()
                )
                val summaryCards : MutableList<SummaryCard> = mutableListOf()
                for (month in months) {
                    val summaryCard = summaryCardDao.getSummaryCards(
                        month.startDate,
                        month.endDate,
                        month.month,
                        month.year
                    )
                    if (summaryCard.monthlyAll != 0.0 && summaryCard.monthlyPositive != 0.0 && summaryCard.monthlyNegative != 0.0) {
                        summaryCards.add(summaryCard)
                    }
                }
                _allSummaryCards.postValue(summaryCards)
            }
        }


    }


    private fun getFirstMovementDate() {

    }
}