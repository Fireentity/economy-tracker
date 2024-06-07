package it.unipd.dei.common_backend.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import it.unipd.dei.common_backend.models.Balance

@Dao
interface BalanceDao {
    @Upsert
    suspend fun insertBalance(balance: Balance)

    @Delete
    suspend fun deleteBalance(balance: Balance)

    @Query("SELECT * FROM balances")
    suspend fun getBalances(): List<Balance>
}