package it.unipd.dei.music_application.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import it.unipd.dei.music_application.daos.CategoryDao
import it.unipd.dei.music_application.daos.MovementDao
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.Movement
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class TestViewModel @Inject constructor(
    private val categoryDao: CategoryDao,
    private val movementDao: MovementDao
) : ViewModel() {

    fun createDummyDataIfNoMovement() {

        viewModelScope.launch {

            if (movementDao.getMovementsCount() == 0) {
                // Crea una categoria fittizia
                val categoryId = UUID.randomUUID()
                val currentTime = System.currentTimeMillis()
                val category = Category(
                    uuid = categoryId,
                    identifier = "Spese",
                    createdAt = currentTime,
                    updatedAt = currentTime
                )

                // Inserisci la categoria nel database
                categoryDao.insertCategory(category)

                // Crea 5 movimenti fittizi associati alla categoria
                val movements = List(10) { index ->
                    Movement(
                        uuid = UUID.randomUUID(),
                        amount = Random.nextInt(-1000, 1000) + Random.nextInt(0, 100) * 0.01,
                        categoryId = categoryId,
                        createdAt = currentTime + index * 1000L, // Tempi leggermente diversi per ogni movimento
                        updatedAt = currentTime + index * 1000L
                    )
                }

                // Inserisci i movimenti nel database
                movements.forEach { movement ->
                    movementDao.insertMovement(movement)
                }

            }
        }
    }

    fun deleteAllMovements() {
        viewModelScope.launch {
            movementDao.deleteAllMovements()
        }
    }
}

