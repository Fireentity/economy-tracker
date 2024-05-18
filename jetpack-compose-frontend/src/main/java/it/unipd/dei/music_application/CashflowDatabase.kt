package it.unipd.dei.music_application

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Cashflow::class],
    version = 1
)
abstract class CashflowDatabase: RoomDatabase() {
    abstract val dao: CashflowDao
}