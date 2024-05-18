package it.unipd.dei.music_application

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CashflowDao {
    @Upsert
    suspend fun insertMoneyMovement(cashflow: Cashflow)

    @Query("SELECT * FROM Cashflow ORDER BY id ASC")
    fun getAllCashflow(): Flow<List<Cashflow>>
}