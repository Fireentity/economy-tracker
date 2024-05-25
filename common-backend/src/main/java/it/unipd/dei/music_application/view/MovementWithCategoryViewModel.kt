package it.unipd.dei.music_application.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipd.dei.music_application.daos.MovementDao
import it.unipd.dei.music_application.models.MovementWithCategory
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovementWithCategoryViewModel @Inject constructor(
    private val movementDao: MovementDao
) : ViewModel() {

    // LiveData for movements with categories
    private val _allMovementsWithCategory = MutableLiveData<List<MovementWithCategory>>()
    val allData: LiveData<List<MovementWithCategory>> = _allMovementsWithCategory

    private val _positiveMovementsWithCategory = MutableLiveData<List<MovementWithCategory>>()
    val positiveData: LiveData<List<MovementWithCategory>> = _positiveMovementsWithCategory

    private val _negativeMovementsWithCategory = MutableLiveData<List<MovementWithCategory>>()
    val negativeData: LiveData<List<MovementWithCategory>> = _negativeMovementsWithCategory

    // LiveData for movementsAmount
    private val _sumAllMovementsAmount = MutableLiveData<Double>()
    val totalAllAmount: LiveData<Double> = _sumAllMovementsAmount

    private val _sumPositiveMovementsAmount = MutableLiveData<Double>()
    val totalPositiveAmount: LiveData<Double> = _sumPositiveMovementsAmount

    private val _sumNegativeMovementsAmount = MutableLiveData<Double>()
    val totalNegativeAmount: LiveData<Double> = _sumNegativeMovementsAmount

    // Offset for pagination
    private var currentAllOffset = 0
    private var currentPositiveOffset = 0
    private var currentNegativeOffset = 0

    // Page size for pagination
    private val pageSize = 7

    // Load a specific type of movements with pagination
    private fun loadMovements(
        offset: Int,
        dataLoader: suspend (Int, Int) -> List<MovementWithCategory>,
        liveData: MutableLiveData<List<MovementWithCategory>>,
        updateOffset: (Int) -> Unit
    ) {
        viewModelScope.launch {
            val loadedData = dataLoader(pageSize, offset)
            if (loadedData.isNotEmpty()) {
                liveData.postValue((liveData.value ?: emptyList()) + loadedData)
                updateOffset(offset + loadedData.size)
            }
        }
    }

    // Load some movements
    fun loadSomeMovements() {
        loadMovements(
            currentAllOffset,
            movementDao::getSomeMovements,
            _allMovementsWithCategory
        ) { newOffset -> currentAllOffset = newOffset }
    }

    // Load some positive movements
    fun loadSomePositiveMovements() {
        loadMovements(
            currentPositiveOffset,
            movementDao::getSomePositiveMovements,
            _positiveMovementsWithCategory
        ) { newOffset -> currentPositiveOffset = newOffset }
    }

    // Load some negative movements
    fun loadSomeNegativeMovements() {
        loadMovements(
            currentNegativeOffset,
            movementDao::getSomeNegativeMovements,
            _negativeMovementsWithCategory
        ) { newOffset -> currentNegativeOffset = newOffset }
    }

    // Load all movements (without pagination)
    private fun loadAllMovements(
        dataLoader: suspend () -> List<MovementWithCategory>,
        liveData: MutableLiveData<List<MovementWithCategory>>
    ) {
        viewModelScope.launch {
            val loadedData = dataLoader()
            liveData.postValue(loadedData)
        }
    }

    private fun getAllMovements() {
        loadAllMovements(movementDao::getAllMovements, _allMovementsWithCategory)
    }

    private fun getAllPositiveMovements() {
        loadAllMovements(movementDao::getAllPositiveMovements, _positiveMovementsWithCategory)
    }

    private fun getAllNegativeMovements() {
        loadAllMovements(movementDao::getAllNegativeMovements, _negativeMovementsWithCategory)
    }

    private fun loadTotalAllAmount() {
        viewModelScope.launch {

            val totalAmount = movementDao.getTotalAmount()
            _sumAllMovementsAmount.postValue(totalAmount)
        }
    }

    private fun loadTotalPositiveAmount() {
        viewModelScope.launch {

            val totalPositive = movementDao.getTotalPositiveAmount()
            _sumPositiveMovementsAmount.postValue(totalPositive)
        }
    }

    private fun loadTotalNegativeAmount() {
        viewModelScope.launch {

            val totalNegative = movementDao.getTotalNegativeAmount()
            _sumNegativeMovementsAmount.postValue(totalNegative)

        }
    }

    fun loadTotalAmounts() {
        loadTotalAllAmount()
        loadTotalPositiveAmount()
        loadTotalNegativeAmount()
    }


    // Initial loading of movements
    fun loadInitialMovements() {
        loadSomeMovements()
        loadSomePositiveMovements()
        loadSomeNegativeMovements()
    }

}
