package it.unipd.dei.common_backend.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipd.dei.common_backend.daos.MovementDao
import it.unipd.dei.common_backend.models.Movement
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovementViewModel @Inject constructor(private val movementDao: MovementDao) : ViewModel() {
    private val _upsertResult = MutableLiveData<Boolean?>()
    val upsertResult: LiveData<Boolean?> = _upsertResult

    fun upsertMovement(movement: Movement) {
        _upsertResult.postValue(null)
        viewModelScope.launch {
            try {
                movementDao.upsertMovement(movement)
                _upsertResult.postValue(true)
            } catch (e: Exception) {
                _upsertResult.postValue(false)
            }
        }
    }
}