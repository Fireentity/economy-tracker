package it.unipd.dei.music_application.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipd.dei.music_application.daos.CategoryDao
import it.unipd.dei.music_application.models.CategoryTotal
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryTotalViewModel @Inject constructor(private val categoryDao: CategoryDao) :
    ViewModel() {

    // LiveData for category total
    private val _allCategoryTotal = MutableLiveData<List<CategoryTotal>>()
    val allData: LiveData<List<CategoryTotal>> = _allCategoryTotal

    //TODO mostrare grafici diversi in base alle pagine?
    fun loadCategoryTotal() {
        viewModelScope.launch {
            val loadedData = categoryDao.getTotalAmountByCategory()
            _allCategoryTotal.postValue(loadedData)
        }
    }
}