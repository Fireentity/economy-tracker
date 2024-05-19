package it.unipd.dei.music_application.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import it.unipd.dei.music_application.models.Balance

@Dao
interface BalanceDao {
    @Upsert
    suspend fun insertBalance(balance: Balance)

    @Delete
    suspend fun deleteBalance(balance: Balance)

    @Query("SELECT * FROM balances")
    suspend fun getBalances(): List<Balance>
}