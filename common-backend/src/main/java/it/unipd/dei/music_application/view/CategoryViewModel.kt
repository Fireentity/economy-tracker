package it.unipd.dei.music_application.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipd.dei.music_application.daos.CategoryDao
import it.unipd.dei.music_application.models.Category
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryDao: CategoryDao) :
    ViewModel() {

    // LiveData for categories
    private val _allCategories = MutableLiveData<List<Category>>()
    val allCategories: LiveData<List<Category>> = _allCategories

    private val _isCategoryIdentifierPresent = MutableLiveData<Boolean?>()
    val isCategoryIdentifierPresent: LiveData<Boolean?> = _isCategoryIdentifierPresent

    private val _upsertResult = MutableLiveData<Boolean?>()
    val upsertResult: LiveData<Boolean?> = _upsertResult

    fun getAllCategories() {
        viewModelScope.launch {
            val loadedData = categoryDao.getAllCategories()
            _allCategories.postValue(loadedData)
        }
    }

    fun isCategoryIdentifierPresent(identifier: String) {
        viewModelScope.launch {
            val categories = categoryDao.getCategoriesByIdentifier(identifier)
            _isCategoryIdentifierPresent.postValue(null)
            if(categories.isNotEmpty()){
                _isCategoryIdentifierPresent.postValue(true)
            } else {
                _isCategoryIdentifierPresent.postValue(false)
            }
        }
    }

    fun upsertCategory(category: Category) {
        _upsertResult.postValue(null)
        viewModelScope.launch {
            try {
                categoryDao.upsertCategory(category)
                _upsertResult.postValue(true)
            } catch (e: Exception) {
                _upsertResult.postValue(false)
            }
        }
    }
}