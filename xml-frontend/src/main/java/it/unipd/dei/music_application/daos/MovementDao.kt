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

    @Query("DELETE FROM movements")
    suspend fun deleteAllMovements()
    @Transaction
    @Query("SELECT * FROM movements WHERE categoryId = :categoryId ORDER BY createdAt")
    suspend fun getMovementsByCategoryOrderedByCreatedAt(categoryId: UUID): List<MovementWithCategory>
    @Transaction
    @Query("SELECT * FROM movements ORDER BY createdAt DESC")
    suspend fun getAllMovements(): List<MovementWithCategory>

    @Transaction
    @Query("SELECT * FROM movements WHERE amount > 0 ORDER BY createdAt DESC")
    suspend fun getAllPositiveMovements(): List<MovementWithCategory>

    @Transaction
    @Query("SELECT * FROM movements WHERE amount < 0 ORDER BY createdAt DESC")
    suspend fun getAllNegativeMovements(): List<MovementWithCategory>

    @Transaction
    @Query("SELECT * FROM movements ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getSomeMovements(limit: Int, offset: Int): List<MovementWithCategory>

    @Transaction
    @Query("SELECT * FROM movements WHERE amount > 0 ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getSomePositiveMovements(limit: Int, offset: Int): List<MovementWithCategory>

    @Transaction
    @Query("SELECT * FROM movements WHERE amount < 0 ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getSomeNegativeMovements(limit: Int, offset: Int): List<MovementWithCategory>
    @Query("SElECT COUNT(uuid) FROM movements")
    suspend fun getMovementsCount(): Int

}