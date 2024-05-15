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

    private val _movementsWithCategory = MutableLiveData<List<MovementWithCategory>>()
    val data: LiveData<List<MovementWithCategory>> = _movementsWithCategory

    fun getAllMovements() {
        viewModelScope.launch {
            val loadedData = movementDao.getAllMovements()
            _movementsWithCategory.postValue(loadedData)
        }
    }
}