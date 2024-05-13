package it.unipd.dei.music_application.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import it.unipd.dei.music_application.models.Balance

@Dao
interface BalanceDao {
    @Upsert
    fun insertBalance(balance: Balance)

    @Delete
    fun deleteBalance(balance: Balance)

    @Query("SELECT * FROM balances")
    fun getBalances(): List<Balance>
}