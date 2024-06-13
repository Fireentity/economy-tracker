package it.unipd.dei.common_backend.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.unipd.dei.common_backend.converters.MonthTypeConverter
import it.unipd.dei.common_backend.daos.BalanceDao
import it.unipd.dei.common_backend.daos.CategoryDao
import it.unipd.dei.common_backend.daos.MovementDao
import it.unipd.dei.common_backend.daos.SummaryDao
import it.unipd.dei.common_backend.models.Balance
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.models.Movement

@Database(version = 1, entities = [Category::class, Movement::class, Balance::class])
@TypeConverters(MonthTypeConverter::class)
abstract class BalanceDatabase : RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDao

    abstract fun getMovementDao(): MovementDao

    abstract fun getBalanceDao(): BalanceDao

    abstract fun getSummaryCardDao() : SummaryDao
}