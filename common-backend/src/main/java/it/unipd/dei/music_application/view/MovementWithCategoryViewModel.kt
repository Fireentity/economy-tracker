package it.unipd.dei.music_application.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipd.dei.music_application.daos.MovementDao
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.Movement
import it.unipd.dei.music_application.models.MovementWithCategory
import it.unipd.dei.music_application.utils.Constants.ALL_CATEGORIES_IDENTIFIER
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MovementWithCategoryViewModel @Inject constructor(
    private val movementDao: MovementDao
) : ViewModel() {

    private val _allMovementsWithCategory = MutableLiveData<List<MovementWithCategory>>()
    val allData: LiveData<List<MovementWithCategory>> = _allMovementsWithCategory

    private val _positiveMovementsWithCategory = MutableLiveData<List<MovementWithCategory>>()
    val positiveData: LiveData<List<MovementWithCategory>> = _positiveMovementsWithCategory

    private val _negativeMovementsWithCategory = MutableLiveData<List<MovementWithCategory>>()
    val negativeData: LiveData<List<MovementWithCategory>> = _negativeMovementsWithCategory

    private val _sumAllMovementsAmount = MutableLiveData<Double>()
    val totalAllAmount: LiveData<Double> = _sumAllMovementsAmount

    private val _sumPositiveMovementsAmount = MutableLiveData<Double>()
    val totalPositiveAmount: LiveData<Double> = _sumPositiveMovementsAmount

    private val _sumNegativeMovementsAmount = MutableLiveData<Double>()
    val totalNegativeAmount: LiveData<Double> = _sumNegativeMovementsAmount

    private var currentAllOffset = 0
    private var currentPositiveOffset = 0
    private var currentNegativeOffset = 0

    private val pageSize = 10


    private fun loadMovements(
        offset: Int,
        updateOffset: (Int) -> Unit,
        dataLoader: suspend (UUID, Int, Int) -> List<MovementWithCategory>,
        liveData: MutableLiveData<List<MovementWithCategory>>,
        category: Category
    ) {
        viewModelScope.launch {
            val loadedData = dataLoader(category.uuid, pageSize, offset)
            if (loadedData.isNotEmpty()) {
                liveData.postValue((liveData.value ?: emptyList()) + loadedData)
                updateOffset(offset + loadedData.size)
            }
        }
    }

    private fun loadMovements(
        offset: Int,
        updateOffset: (Int) -> Unit,
        dataLoader: suspend (Int, Int) -> List<MovementWithCategory>,
        liveData: MutableLiveData<List<MovementWithCategory>>
    ) {
        viewModelScope.launch {
            val loadedData = dataLoader(pageSize, offset)
            if (loadedData.isNotEmpty()) {
                liveData.postValue((liveData.value ?: emptyList()) + loadedData)
                updateOffset(offset + loadedData.size)
            }
        }
    }


    fun loadSomeMovementsByCategory(category: Category) {
        if (category.identifier == ALL_CATEGORIES_IDENTIFIER) {
            loadMovements(currentAllOffset, {
                currentAllOffset = it
            }, movementDao::getSomeMovements, _allMovementsWithCategory)
        } else {
            loadMovements(currentAllOffset, {
                currentAllOffset = it
            }, movementDao::getSomeMovementsByCategory, _allMovementsWithCategory, category)
        }
    }

    fun loadSomePositiveMovementsByCategory(category: Category) {
        if (category.identifier == ALL_CATEGORIES_IDENTIFIER) {
            loadMovements(currentPositiveOffset, {
                currentPositiveOffset = it
            }, movementDao::getSomePositiveMovements, _positiveMovementsWithCategory)
        } else {
            loadMovements(
                currentPositiveOffset,
                {
                    currentPositiveOffset = it
                },
                movementDao::getSomePositiveMovementsByCategory,
                _positiveMovementsWithCategory,
                category
            )
        }
    }

    fun loadSomeNegativeMovementsByCategory(category: Category) {
        if (category.identifier == ALL_CATEGORIES_IDENTIFIER) {
            loadMovements(currentNegativeOffset, {
                currentNegativeOffset = it
            }, movementDao::getSomeNegativeMovements, _negativeMovementsWithCategory)
        } else {
            loadMovements(
                currentNegativeOffset,
                {
                    currentNegativeOffset = it
                },
                movementDao::getSomeNegativeMovementsByCategory,
                _negativeMovementsWithCategory,
                category
            )
        }
    }

    private fun loadTotalAmount(
        amountLoader: suspend () -> Double,
        liveData: MutableLiveData<Double>
    ) {
        viewModelScope.launch {
            val totalAmount = amountLoader()
            liveData.postValue(totalAmount)
        }
    }

    private fun loadTotalAmount(
        amountLoader: suspend (UUID) -> Double,
        liveData: MutableLiveData<Double>,
        category: Category
    ) {
        viewModelScope.launch {
            val totalAmount = amountLoader(category.uuid)
            liveData.postValue(totalAmount)
        }
    }

    fun loadTotalAmountsByCategory(category: Category) {
        if (category.identifier == ALL_CATEGORIES_IDENTIFIER) {
            loadTotalAmount(movementDao::getTotalAmount, _sumAllMovementsAmount)
            loadTotalAmount(movementDao::getTotalPositiveAmount, _sumPositiveMovementsAmount)
            loadTotalAmount(movementDao::getTotalNegativeAmount, _sumNegativeMovementsAmount)
        } else {
            loadTotalAmount(
                movementDao::getTotalAmountByCategory,
                _sumAllMovementsAmount,
                category
            )
            loadTotalAmount(
                movementDao::getTotalPositiveAmountByCategory,
                _sumPositiveMovementsAmount,
                category
            )
            loadTotalAmount(
                movementDao::getTotalNegativeAmountByCategory,
                _sumNegativeMovementsAmount,
                category
            )
        }
    }

    fun loadInitialMovementsByCategory(category: Category) {
        currentAllOffset = 0
        currentPositiveOffset = 0
        currentNegativeOffset = 0

        _allMovementsWithCategory.postValue(emptyList())
        _positiveMovementsWithCategory.postValue(emptyList())
        _negativeMovementsWithCategory.postValue(emptyList())

        loadSomeMovementsByCategory(category)
        loadSomePositiveMovementsByCategory(category)
        loadSomeNegativeMovementsByCategory(category)
    }

    private fun loadAllMovements(
        dataLoader: suspend () -> List<MovementWithCategory>,
        liveData: MutableLiveData<List<MovementWithCategory>>
    ) {
        viewModelScope.launch {
            val loadedData = dataLoader()
            liveData.postValue(loadedData)
        }
    }

    private val _insertResult = MutableLiveData<Boolean?>()
    val insertResult: LiveData<Boolean?> get() = _insertResult

    fun insertMovement(movement: Movement) {
        _insertResult.postValue(null)
        viewModelScope.launch {
            try {
                movementDao.insertMovement(movement)
                _insertResult.postValue(true)
            } catch (e: Exception) {
                _insertResult.postValue(false)
            }
        }
    }

}
