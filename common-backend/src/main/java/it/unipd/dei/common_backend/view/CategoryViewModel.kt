package it.unipd.dei.common_backend.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipd.dei.common_backend.daos.CategoryDao
import it.unipd.dei.common_backend.models.Category
import kotlinx.coroutines.launch
import java.sql.SQLException
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryDao: CategoryDao) :
    ViewModel() {

    private val _allCategories = MutableLiveData<Map<String, Category>>()
    val allCategories: LiveData<Map<String, Category>> = _allCategories

    fun loadAllCategories() {
        viewModelScope.launch {
            val loadedData = categoryDao.getAllCategories().associateBy { it.identifier }
            _allCategories.postValue(loadedData)
        }
    }

    fun getCategoryByIdentifier(identifier: String): Category? {
        return _allCategories.value?.get(identifier);
    }

    fun isCategoryIdentifierPresent(identifier: String): Boolean {
        return getCategoryByIdentifier(identifier) == null;
    }

    //TODO also modify in ram
    fun upsertCategory(
        category: Category,
        onSuccess: () -> Unit,
        onThrow: (e: SQLException) -> Unit
    ) {
        viewModelScope.launch {
            try {
                categoryDao.upsertCategory(category);
                onSuccess()
            } catch (e: SQLException) {
                onThrow(e)
            }
        }
    }

    //TODO also modify in ram

    fun deleteCategory(
        category: Category
    ) {
        deleteCategory(category, {}, {})
    }

    fun deleteCategory(
        category: Category,
        onSuccess: () -> Unit,
        onThrow: (e: SQLException) -> Unit
    ) {
        viewModelScope.launch {
            try {
                categoryDao.deleteCategory(category)
                onSuccess()
            } catch (e: SQLException) {
                onThrow(e)
            }
        }
    }
}