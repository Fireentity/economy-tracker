package it.unipd.dei.common_backend.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipd.dei.common_backend.daos.MovementDao
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.models.Movement
import it.unipd.dei.common_backend.models.MovementWithCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.SQLException
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
    private val categoryToFilter = MutableLiveData<Category?>()

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

    fun addCategoryFilter(category: Category) {
        categoryToFilter.postValue(category)
    }

    fun removeCategoryFilter() {
        categoryToFilter.postValue(null)
    }

    fun loadSomeMovementsByCategory(then: () -> Unit) {
        viewModelScope.launch {
            val categoryUuid: UUID? = categoryToFilter.value?.uuid
            movements.postValue(
                movements.value?.plus(
                    if (categoryUuid == null) {
                        movementDao.getSomeMovements(
                            PAGE_SIZE,
                            positiveMovements.value?.size ?: 0
                        )
                    } else {
                        movementDao.getSomeMovementsByCategory(
                            categoryUuid,
                            PAGE_SIZE,
                            positiveMovements.value?.size ?: 0
                        )
                    }
                )
            )
            then()
        }
    }

    fun loadSomePositiveMovementsByCategory(then: () -> Unit) {
        viewModelScope.launch {
            val categoryUuid: UUID? = categoryToFilter.value?.uuid
            positiveMovements.postValue(
                positiveMovements.value?.plus(
                    if (categoryUuid == null) {
                        movementDao.getSomeMovements(
                            PAGE_SIZE,
                            positiveMovements.value?.size ?: 0
                        )
                    } else {
                        movementDao.getSomePositiveMovementsByCategory(
                            categoryUuid,
                            PAGE_SIZE,
                            positiveMovements.value?.size ?: 0
                        )
                    }
                )
            )
            then()
        }
    }

    fun loadSomeNegativeMovementsByCategory(then: () -> Unit) {
        viewModelScope.launch {
            val categoryUuid: UUID? = categoryToFilter.value?.uuid
            negativeMovements.postValue(
                negativeMovements.value?.plus(
                    if (categoryUuid == null) {
                        movementDao.getSomeNegativeMovements(
                            PAGE_SIZE,
                            negativeMovements.value?.size ?: 0
                        )
                    } else {
                        movementDao.getSomeNegativeMovementsByCategory(
                            categoryUuid,
                            PAGE_SIZE,
                            negativeMovements.value?.size ?: 0
                        )
                    }
                )
            )
            then()
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

    fun loadInitialMovementsByCategory() {
        loadSomeMovementsByCategory {}
        loadSomePositiveMovementsByCategory {}
        loadSomeNegativeMovementsByCategory {}
    }

    fun upsertMovement(
        movement: Movement,
        onSuccess: () -> Unit,
        onFailure: (e: SQLException) -> Unit
    ) {
        viewModelScope.launch {
            try {
                movementDao.upsertMovement(movement)
                onSuccess()
            } catch (e: SQLException) {
                onFailure(e)
            }
        }
    }

    //TODO Modificare lo stato e rendere tutto thread-safe

    fun deleteMovement(
        movement: Movement,
        onSuccess: () -> Unit,
        onFailure: (e: SQLException) -> Unit
    ) {
        viewModelScope.launch {
            try {
                movementDao.deleteMovement(movement)
                onSuccess()
            } catch (e: SQLException) {
                onFailure(e)
            }
        }
    }

    fun deleteMovement(
        movement: Movement
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                movementDao.deleteMovement(movement)
            }
        }
    }
}
