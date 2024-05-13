package it.unipd.dei.music_application.database

import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.unipd.dei.music_application.App
import it.unipd.dei.music_application.daos.CategoryDao
import it.unipd.dei.music_application.daos.MovementDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: App): BalanceDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            BalanceDatabase::class.java, "balance-database"
        )
/*            .setQueryCallback({ sql, bindArgs ->
                // This is where you log your query
                Log.d("Database Query", "sql: $sql")
                Log.d("Database Query", "args: ${bindArgs.joinToString()}")
            }, Executors.newSingleThreadExecutor())*/
            .build();
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: BalanceDatabase): CategoryDao {
        return database.getCategoryDao()
    }

    @Provides
    @Singleton
    fun provideMovementDao(database: BalanceDatabase): MovementDao {
        return database.getMovementDao()
    }
}