package it.unipd.dei.music_application.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipd.dei.music_application.daos.CategoryDao
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.CategoryTotal
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryDao: CategoryDao) :
    ViewModel() {

    // LiveData for categories
    private val _allCategories = MutableLiveData<List<Category>>()
    val allCategories: LiveData<List<Category>> = _allCategories


    fun getAllCategories() {
        viewModelScope.launch {
            val loadedData = categoryDao.getAllCategories()
            _allCategories.postValue(loadedData)
        }
    }
}