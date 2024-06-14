package it.unipd.dei.common_backend.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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
) : ViewModel() {
    private val _allSummaryCards = MutableLiveData<List<Summary>>(emptyList())
    val allSummary: LiveData<List<Summary>> = _allSummaryCards
    private val _currentMonthSummary = MutableLiveData(Summary.DEFAULT)
    val currentMonthSummary: LiveData<Summary> = _currentMonthSummary

    fun loadAllSummaries() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val summaries: List<Summary> = summaryDao.getSummary()
                withContext(Dispatchers.Main) {
                    _allSummaryCards.postValue(summaries)
                }
            }
        }
    }

    fun loadSummaryForCurrentMonth() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val month = DateHelper.getCurrentMonth()
                val year = DateHelper.getCurrentYear()
                val loadedData = summaryDao.getSummaryByMonthAndYear(month.value, year)
                withContext(Dispatchers.Main) {
                    _currentMonthSummary.postValue(loadedData ?: Summary.DEFAULT)
                }
            }
        }
    }

    fun invalidateSummariesAndReload() {
        loadAllSummaries()
        loadSummaryForCurrentMonth()
    }

}