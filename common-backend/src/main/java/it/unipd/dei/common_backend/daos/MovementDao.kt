package it.unipd.dei.common_backend.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import it.unipd.dei.common_backend.models.Movement
import it.unipd.dei.common_backend.models.MovementWithCategory
import java.util.UUID

@Dao
interface MovementDao {

    @Upsert
    suspend fun upsertMovement(movement: Movement)

    @Delete
    suspend fun deleteMovement(movement: Movement)

    @Query("DELETE FROM movements")
    suspend fun deleteAllMovements()

    @Transaction
    @Query("SELECT * FROM movements WHERE categoryId = :categoryId ORDER BY date")
    suspend fun getMovementsByCategoryOrderedByDate(categoryId: UUID): List<MovementWithCategory>

    @Transaction
    @Query("SELECT * FROM movements ORDER BY date DESC")
    suspend fun getAllMovements(): List<MovementWithCategory>

    @Transaction
    @Query("SELECT * FROM movements WHERE amount > 0 ORDER BY date DESC")
    suspend fun getAllPositiveMovements(): List<MovementWithCategory>

    @Transaction
    @Query("SELECT * FROM movements WHERE amount < 0 ORDER BY date DESC")
    suspend fun getAllNegativeMovements(): List<MovementWithCategory>

    @Transaction
    @Query("SELECT SUM(amount) FROM movements")
    suspend fun getTotalAmount(): Double


    @Transaction
    @Query("SELECT SUM(amount) FROM movements WHERE amount > 0")
    suspend fun getTotalPositiveAmount(): Double

    @Transaction
    @Query("SELECT SUM(amount) FROM movements WHERE amount < 0")
    suspend fun getTotalNegativeAmount(): Double

    @Transaction
    @Query("SELECT SUM(amount) FROM movements WHERE categoryId = :categoryId")
    suspend fun getTotalAmountByCategory(categoryId: UUID): Double


    @Transaction
    @Query("SELECT SUM(amount) FROM movements WHERE amount > 0 AND categoryId = :categoryId")
    suspend fun getTotalPositiveAmountByCategory(categoryId: UUID): Double

    @Transaction
    @Query("SELECT SUM(amount) FROM movements WHERE amount < 0 AND categoryId = :categoryId")
    suspend fun getTotalNegativeAmountByCategory(categoryId: UUID): Double


    @Transaction
    @Query("SELECT * FROM movements ORDER BY date DESC LIMIT :limit OFFSET :offset")
    suspend fun getSomeMovements(limit: Int, offset: Int): List<MovementWithCategory>

    @Transaction
    @Query("SELECT * FROM movements WHERE amount > 0 ORDER BY date DESC LIMIT :limit OFFSET :offset")
    suspend fun getSomePositiveMovements(limit: Int, offset: Int): List<MovementWithCategory>

    @Transaction
    @Query("SELECT * FROM movements WHERE amount < 0 ORDER BY date DESC LIMIT :limit OFFSET :offset")
    suspend fun getSomeNegativeMovements(limit: Int, offset: Int): List<MovementWithCategory>

    @Query("SElECT COUNT(uuid) FROM movements")
    suspend fun getMovementsCount(): Int

    @Transaction
    @Query("SELECT * FROM movements WHERE categoryId = :categoryId ORDER BY date DESC LIMIT :limit OFFSET :offset")
    suspend fun getSomeMovementsByCategory(
        categoryId: UUID,
        limit: Int,
        offset: Int
    ): List<MovementWithCategory>

    @Transaction
    @Query("SELECT * FROM movements WHERE amount > 0 AND categoryId = :categoryId ORDER BY date DESC LIMIT :limit OFFSET :offset")
    suspend fun getSomePositiveMovementsByCategory(
        categoryId: UUID,
        limit: Int,
        offset: Int
    ): List<MovementWithCategory>

    @Transaction
    @Query("SELECT * FROM movements WHERE amount < 0 AND categoryId = :categoryId ORDER BY date DESC LIMIT :limit OFFSET :offset")
    suspend fun getSomeNegativeMovementsByCategory(
        categoryId: UUID,
        limit: Int,
        offset: Int
    ): List<MovementWithCategory>

    @Transaction
    @Query("SELECT date FROM movements ORDER BY date DESC LIMIT 1")
    suspend fun getFirstMovementDate() : Long

    @Transaction
    @Query("SELECT date FROM movements ORDER BY date ASC LIMIT 1")
    suspend fun getLastMovementDate(): Long

}
