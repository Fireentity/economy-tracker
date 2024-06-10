package it.unipd.dei.common_backend.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipd.dei.common_backend.daos.CategoryDao
import it.unipd.dei.common_backend.daos.MovementDao
import it.unipd.dei.common_backend.models.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.SQLException
import java.util.TreeMap
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryDao: CategoryDao,
) : ViewModel() {

    private val _allCategories = MutableLiveData<Map<String, Category>>(emptyMap())
    val allCategories: LiveData<Map<String, Category>> = _allCategories

    fun loadAllCategories() {
        viewModelScope.launch {
            val categories: TreeMap<String, Category> = TreeMap()
            categoryDao.getAllCategories().associateByTo(categories) { it.identifier }
            _allCategories.value = categories
        }
    }

    fun getCategoryByIdentifier(identifier: String): Category? {
        return _allCategories.value?.get(identifier);
    }

    fun upsertCategory(
        category: Category,
        onSuccess: () -> Unit,
        onThrow: (e: SQLException) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    categoryDao.upsertCategory(category);
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            _allCategories.value = _allCategories.value?.plus(Pair(category.identifier, category))
                            invalidateCategoriesAndReload()
                            onSuccess()
                        }
                    }

                } catch (e: SQLException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            onThrow(e)
                        }
                    }
                }
            }
        }
    }

    fun deleteCategory(
        category: Category,
        onSuccess: () -> Unit,
        onThrow: (e: SQLException) -> Unit
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    categoryDao.deleteCategory(category)
                    withContext(Dispatchers.Main) {
                        _allCategories.value = _allCategories.value?.minus(category.identifier)
                        invalidateCategoriesAndReload()
                        onSuccess()
                    }
                } catch (e: SQLException) {
                    viewModelScope.launch {
                        withContext(Dispatchers.Main) {
                            onThrow(e)
                        }
                    }
                }
            }
        }
    }


    fun invalidateCategories() {
        _allCategories.value = emptyMap()
    }

    fun invalidateCategoriesAndReload() {
        invalidateCategories()
        loadAllCategories()
        //TODO fix here
        //movementWithCategoryViewModel.invalidateMovementsAndReload()
    }
}