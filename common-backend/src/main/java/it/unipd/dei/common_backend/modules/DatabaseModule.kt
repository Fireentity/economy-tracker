package it.unipd.dei.common_backend.modules

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.unipd.dei.common_backend.daos.CategoryDao
import it.unipd.dei.common_backend.daos.MovementDao
import it.unipd.dei.common_backend.daos.SummaryCardDao
import it.unipd.dei.common_backend.database.BalanceDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): BalanceDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            BalanceDatabase::class.java, "balance-database"
        ).allowMainThreadQueries().build();
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
    @Provides
    @Singleton
    fun provideSummaryCardDao(database: BalanceDatabase): SummaryCardDao {
        return database.getSummaryCardDao()
    }
}