package it.unipd.dei.common_backend.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipd.dei.common_backend.daos.MovementDao
import it.unipd.dei.common_backend.models.Category
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

    private val movements = MutableLiveData<MutableList<MovementWithCategory>>(ArrayList(PAGE_SIZE))
    private val positiveMovements =
        MutableLiveData<MutableList<MovementWithCategory>>(ArrayList(PAGE_SIZE))
    private val negativeMovements =
        MutableLiveData<MutableList<MovementWithCategory>>(ArrayList(PAGE_SIZE))
    private val totalBalance = MutableLiveData<Double>()
    private val earnedMoney = MutableLiveData<Double>()
    private val spentMoney = MutableLiveData<Double>()
    private val categoryToFilter = MutableLiveData<Category?>()

    companion object {
        private const val PAGE_SIZE: Int = 20;
    }

    fun getCategoryToFilter(): LiveData<Category?> {
        return categoryToFilter
    }

    fun getMovements(): LiveData<out List<MovementWithCategory>> {
        return movements
    }

    fun getPositiveMovements(): LiveData<out List<MovementWithCategory>> {
        return positiveMovements
    }

    fun getNegativeMovements(): LiveData<out List<MovementWithCategory>> {
        return negativeMovements
    }


    fun addCategoryFilter(category: Category) {
        categoryToFilter.value = category
    }

    fun removeCategoryFilter() {
        categoryToFilter.value = null
    }

    fun loadSomeMovementsByCategory(then: () -> Unit) {
        viewModelScope.launch {
            val categoryUuid: UUID? = categoryToFilter.value?.uuid
            withContext(Dispatchers.IO) {
                val result = loadSomeMovements(
                    categoryUuid,
                    PAGE_SIZE,
                    movements.value?.size ?: 0
                )
                withContext(Dispatchers.Main) {
                    movements.value?.addAll(result)
                    then()
                }
            }
        }
    }

    private suspend fun loadSomeMovements(
        categoryUuid: UUID?,
        limit: Int,
        offset: Int
    ): List<MovementWithCategory> {
        if (categoryUuid != null) {
            return movementDao.getSomeMovementsByCategory(
                categoryUuid,
                limit,
                offset
            )
        }
        return movementDao.getSomeMovements(
            limit,
            offset
        )
    }

    fun loadSomePositiveMovementsByCategory(then: () -> Unit) {
        viewModelScope.launch {
            val categoryUuid: UUID? = categoryToFilter.value?.uuid
            withContext(Dispatchers.IO) {
                val result = loadSomePositiveMovements(
                    categoryUuid,
                    PAGE_SIZE,
                    positiveMovements.value?.size ?: 0
                )
                withContext(Dispatchers.Main) {
                    positiveMovements.value?.addAll(result)
                    then()
                }
            }
        }
    }

    private suspend fun loadSomePositiveMovements(
        categoryUuid: UUID?,
        limit: Int,
        offset: Int
    ): List<MovementWithCategory> {
        if (categoryUuid != null) {
            return movementDao.getSomePositiveMovementsByCategory(
                categoryUuid,
                limit,
                offset
            )
        }
        return movementDao.getSomePositiveMovements(
            limit,
            offset
        )
    }

    fun loadSomeNegativeMovementsByCategory(then: () -> Unit) {
        viewModelScope.launch {
            val categoryUuid: UUID? = categoryToFilter.value?.uuid
            withContext(Dispatchers.IO) {
                val result = loadSomeNegativeMovements(
                    categoryUuid,
                    PAGE_SIZE,
                    negativeMovements.value?.size ?: 0
                )
                withContext(Dispatchers.Main) {
                    negativeMovements.value?.addAll(result)
                    then()
                }
            }
        }
    }

    private suspend fun loadSomeNegativeMovements(
        categoryUuid: UUID?,
        limit: Int,
        offset: Int
    ): List<MovementWithCategory> {
        if (categoryUuid != null) {
            return movementDao.getSomeNegativeMovementsByCategory(
                categoryUuid,
                limit,
                offset
            )
        }
        return movementDao.getSomeNegativeMovements(
            limit,
            offset
        )
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
        movement: MovementWithCategory,
        onSuccess: () -> Unit,
        onFailure: (e: SQLException) -> Unit
    ) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    movementDao.upsertMovement(movement.movement)
                    withContext(Dispatchers.Main) {
                        movement.upsertIfNegative(negativeMovements.value!!)
                        movement.upsertIfPositive(positiveMovements.value!!)
                        movement.upsert(movements.value!!)
                        onSuccess()
                    }
                }
            } catch (e: SQLException) {
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }
            }
        }
    }

    fun deleteMovement(
        movement: MovementWithCategory,
        onSuccess: () -> Unit,
        onFailure: (e: SQLException) -> Unit
    ) {

        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    movementDao.upsertMovement(movement.movement)
                    withContext(Dispatchers.Main) {
                        movement.deleteIfNegative(negativeMovements.value!!)
                        movement.deleteIfPositive(positiveMovements.value!!)
                        movement.delete(movements.value!!)
                        onSuccess()
                    }
                }
            } catch (e: SQLException) {
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }
            }
        }
    }

    private fun invalidateMovements() {
        movements.value?.clear()
        positiveMovements.value?.clear()
        negativeMovements.value?.clear()
    }

    fun invalidateMovementsAndReload() {
        invalidateMovements();
        loadInitialMovementsByCategory()
    }
}
