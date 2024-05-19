package it.unipd.dei.music_application.ui

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
    private fun loadAllMovements(dataLoader: suspend () -> List<MovementWithCategory>, liveData: MutableLiveData<List<MovementWithCategory>>) {
        viewModelScope.launch {
            val loadedData = dataLoader()
            liveData.postValue(loadedData)
        }
    }

    // Load all movements
    private fun getAllMovements() {
        loadAllMovements(movementDao::getAllMovements, _allMovementsWithCategory)
    }

    // Load all positive movements
    private fun getAllPositiveMovements() {
        loadAllMovements(movementDao::getAllPositiveMovements, _positiveMovementsWithCategory)
    }

    // Load all negative movements
    private fun getAllNegativeMovements() {
        loadAllMovements(movementDao::getAllNegativeMovements, _negativeMovementsWithCategory)
    }

    // Initial loading of movements
    fun getMovements() {
        loadSomeMovements()
        loadSomePositiveMovements()
        loadSomeNegativeMovements()
    }
}
