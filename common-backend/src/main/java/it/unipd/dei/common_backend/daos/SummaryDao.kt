package it.unipd.dei.common_backend.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import it.unipd.dei.common_backend.models.Summary
import java.time.Month

@Dao
interface SummaryDao {
    @Transaction
    @Query("""
        SELECT 
            SUM(amount) AS monthlyAll,
            SUM(CASE WHEN amount > 0 THEN amount ELSE 0 END) AS monthlyPositive,
            SUM(CASE WHEN amount < 0 THEN amount ELSE 0 END) AS monthlyNegative,
            :month AS month,
            :year AS year
        FROM movements
        WHERE date BETWEEN :startDate AND :endDate
    """)
    suspend fun getSummary(startDate: Long, endDate: Long, month: Month, year: Int): Summary

}