package it.unipd.dei.music_application.view

import android.icu.text.Transliterator.Position
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
    private val movementDao: MovementDao,
) : ViewModel() {

    private val movements = MutableLiveData<List<MovementWithCategory>>(emptyList())
    private val positiveMovements = MutableLiveData<List<MovementWithCategory>>(emptyList())
    private val negativeMovements = MutableLiveData<List<MovementWithCategory>>(emptyList())
    private val totalBalance = MutableLiveData<Double>()
    private val earnedMoney = MutableLiveData<Double>()
    private val spentMoney = MutableLiveData<Double>()
    private val _insertResult = MutableLiveData<Boolean?>()
    private val _deleteResult = MutableLiveData<Boolean?>()

    companion object {
        private const val PAGE_SIZE: Int = 10;
    }

    fun getMovements(): LiveData<List<MovementWithCategory>> {
        return movements
    }

    fun getNegativeMovement(): LiveData<List<MovementWithCategory>> {
        return negativeMovements
    }

    fun getPositiveMovement(): LiveData<List<MovementWithCategory>> {
        return positiveMovements
    }

    private fun loadSomeMovements() {
        viewModelScope.launch {
            movements.postValue(
                movements.value?.plus(
                    movementDao.getSomeMovements(
                        PAGE_SIZE,
                        movements.value?.size ?: 0
                    )
                )
            )
        }
    }

    private fun loadSomePositiveMovements() {
        viewModelScope.launch {
            positiveMovements.postValue(
                positiveMovements.value?.plus(
                    movementDao.getSomePositiveMovements(
                        PAGE_SIZE,
                        positiveMovements.value?.size ?: 0
                    )
                )
            )
        }
    }

    private fun loadSomeNegativeMovements() {
        viewModelScope.launch {
            negativeMovements.postValue(
                negativeMovements.value?.plus(
                    movementDao.getSomeNegativeMovements(
                        PAGE_SIZE,
                        negativeMovements.value?.size ?: 0
                    )
                )
            )
        }
    }

    fun loadSomeMovementsByCategory(category: Category) {
        viewModelScope.launch {
            movements.postValue(
                movements.value?.plus(
                    movementDao.getSomeMovementsByCategory(
                        category.uuid,
                        PAGE_SIZE,
                        movements.value?.size ?: 0
                    )
                )
            )
        }
    }

    fun loadSomePositiveMovementsByCategory(category: Category) {
        viewModelScope.launch {
            positiveMovements.postValue(
                positiveMovements.value?.plus(
                    movementDao.getSomePositiveMovementsByCategory(
                        category.uuid,
                        PAGE_SIZE,
                        positiveMovements.value?.size ?: 0
                    )
                )
            )
        }
    }

    fun loadSomeNegativeMovementsByCategory(category: Category) {
        viewModelScope.launch {
            negativeMovements.postValue(
                negativeMovements.value?.plus(
                    movementDao.getSomeNegativeMovementsByCategory(
                        category.uuid,
                        PAGE_SIZE,
                        negativeMovements.value?.size ?: 0
                    )
                )
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
            loadTotalAmount(movementDao::getTotalAmount, totalBalance)
            loadTotalAmount(movementDao::getTotalPositiveAmount, earnedMoney)
            loadTotalAmount(movementDao::getTotalNegativeAmount, spentMoney)
        } else {
            loadTotalAmount(
                movementDao::getTotalAmountByCategory,
                totalBalance,
                category
            )
            loadTotalAmount(
                movementDao::getTotalPositiveAmountByCategory,
                earnedMoney,
                category
            )
            loadTotalAmount(
                movementDao::getTotalNegativeAmountByCategory,
                spentMoney,
                category
            )
        }
    }

    fun loadInitialMovementsByCategory(category: Category) {
        loadSomeMovementsByCategory(category)
        loadSomePositiveMovementsByCategory(category)
        loadSomeNegativeMovementsByCategory(category)
    }

    fun loadInitialMovements() {
        loadSomeNegativeMovements()
        loadSomePositiveMovements()
        loadSomeMovements()
    }

    fun upsertMovement(movement: Movement) {
        _insertResult.postValue(null)
        viewModelScope.launch {
            try {
                movementDao.upsertMovement(movement)
                _insertResult.postValue(true)
            } catch (e: Exception) {
                _insertResult.postValue(false)
            }
        }
    }

    fun deleteMovement(movement: Movement){
        _deleteResult.postValue(null)
        viewModelScope.launch {
            try {
                movementDao.deleteMovement(movement)
                _deleteResult.postValue(true)
            } catch (e:Exception){
                _deleteResult.postValue(false)
            }
        }
    }
}
