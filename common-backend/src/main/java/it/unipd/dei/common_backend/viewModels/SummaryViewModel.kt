package it.unipd.dei.common_backend.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipd.dei.common_backend.daos.MovementDao
import it.unipd.dei.common_backend.daos.SummaryDao
import it.unipd.dei.common_backend.models.Summary
import it.unipd.dei.common_backend.utils.DateHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val summaryDao: SummaryDao,
    private val movementDao: MovementDao
) : ViewModel() {
    private val _allSummaryCards = MutableLiveData<List<Summary>>(emptyList())
    val allSummary: LiveData<List<Summary>> = _allSummaryCards


    fun loadAllSummaryCards() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val months = DateHelper.getMonthlyIntervals(
                    movementDao.getLastMovementDate(),
                    movementDao.getFirstMovementDate()
                )
                val summaries: MutableList<Summary> = mutableListOf()
                for (month in months) {
                    val summaryCard = summaryDao.getSummaryCards(
                        month.startDate,
                        month.endDate,
                        month.month,
                        month.year
                    )
                    if (summaryCard.monthlyPositive != 0.0 || summaryCard.monthlyNegative != 0.0) {
                        summaries.add(summaryCard)
                    }
                }
                if (_allSummaryCards.value != summaries) {
                    _allSummaryCards.postValue(summaries)
                }
            }
        }


    }

}