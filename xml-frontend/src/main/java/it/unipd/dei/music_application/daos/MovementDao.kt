package it.unipd.dei.music_application.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import it.unipd.dei.music_application.models.Movement
import it.unipd.dei.music_application.models.MovementWithCategory
import java.util.UUID

@Dao
interface MovementDao {

    @Upsert
    suspend fun insertMovement(movement: Movement)

    @Delete
    suspend fun deleteMovement(movement: Movement)

    @Transaction
    @Query("SELECT * FROM movements WHERE categoryId = :categoryId ORDER BY createdAt")
    suspend fun getMovementsByCategoryOrderedByCreatedAt(categoryId: UUID): List<MovementWithCategory>

    @Transaction
    @Query("SELECT * FROM movements")
    suspend fun getAllMovements(): List<MovementWithCategory>
}