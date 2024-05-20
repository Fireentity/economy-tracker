package it.unipd.dei.music_application.database

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import it.unipd.dei.music_application.daos.BalanceDao
import it.unipd.dei.music_application.daos.CategoryDao
import it.unipd.dei.music_application.daos.MovementDao
import it.unipd.dei.music_application.models.Balance
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.Movement

@Database(version = 1, entities = [Category::class, Movement::class, Balance::class])
abstract class BalanceDatabase : RoomDatabase() {

    abstract fun getCategoryDao(): CategoryDao

    abstract fun getMovementDao(): MovementDao

    abstract fun getBalanceDao(): BalanceDao

    override fun clearAllTables() {
        TODO("Not yet implemented")
    }

    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }

    override fun createOpenHelper(config: DatabaseConfiguration): SupportSQLiteOpenHelper {
        TODO("Not yet implemented")
    }
}