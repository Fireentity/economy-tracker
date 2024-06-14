package it.unipd.dei.common_backend.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import it.unipd.dei.common_backend.models.Summary

@Dao
interface SummaryDao {
    @Transaction
    @Query(
        """
        SELECT 
            SUM(amount) AS monthlyAll,
            SUM(CASE WHEN amount > 0 THEN amount ELSE 0 END) AS monthlyPositive,
            SUM(CASE WHEN amount < 0 THEN amount ELSE 0 END) AS monthlyNegative,
            CAST(STRFTIME('%m', datetime(date/1000, 'unixepoch')) AS INTEGER) AS month,
            CAST(STRFTIME('%Y', datetime(date/1000, 'unixepoch')) AS INTEGER) AS year
        FROM movements
        GROUP BY month, year
        ORDER BY year DESC, month DESC
    """
    )
    suspend fun getSummary(): List<Summary>

    @Transaction
    @Query(
        """
        SELECT 
            SUM(amount) AS monthlyAll,
            SUM(CASE WHEN amount > 0 THEN amount ELSE 0 END) AS monthlyPositive,
            SUM(CASE WHEN amount < 0 THEN amount ELSE 0 END) AS monthlyNegative,
            CAST(STRFTIME('%M', datetime(date/1000, 'unixepoch')) AS INTEGER) AS month,
            CAST(STRFTIME('%Y', datetime(date/1000, 'unixepoch')) AS INTEGER) AS year
        FROM movements
        WHERE month = :month AND year = :year
        GROUP BY month, year
    """
    )
    suspend fun getSummaryByMonthAndYear(month: Int, year: Int): Summary

}