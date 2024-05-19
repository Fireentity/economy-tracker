package it.unipd.dei.music_application

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CashflowDao {
    @Upsert
    suspend fun insertMoneyMovement(cashflow: Cashflow)

    @Delete
    suspend fun deleteMoneyMovement(cashflow: Cashflow)

    @Query("SELECT * FROM Cashflow ORDER BY id ASC")
    fun getAllCashflowOrderedById(): Flow<List<Cashflow>>
}