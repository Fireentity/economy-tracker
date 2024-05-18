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

    private val _allMovementsWithCategory = MutableLiveData<List<MovementWithCategory>>()
    val allData: LiveData<List<MovementWithCategory>> = _allMovementsWithCategory

    private val _positiveMovementsWithCategory = MutableLiveData<List<MovementWithCategory>>()
    val positiveData: LiveData<List<MovementWithCategory>> = _positiveMovementsWithCategory

    private val _negativeMovementsWithCategory = MutableLiveData<List<MovementWithCategory>>()
    val negativeData: LiveData<List<MovementWithCategory>> = _negativeMovementsWithCategory

    private fun getAllMovements() {
        viewModelScope.launch {
            val loadedData = movementDao.getAllMovements()
            _allMovementsWithCategory.postValue(loadedData)
        }
    }
    private fun getAllPositiveMovements() {
        viewModelScope.launch {
            val loadedData = movementDao.getAllPositiveMovements()
            _positiveMovementsWithCategory.postValue(loadedData)
        }
    }
    private fun getAllNegativeMovements() {
        viewModelScope.launch {
            val loadedData = movementDao.getAllNegativeMovements()
            _negativeMovementsWithCategory.postValue(loadedData)
        }
    }
    fun getMovements(){
        this.getAllMovements()
        this.getAllPositiveMovements()
        this.getAllNegativeMovements()
    }
}