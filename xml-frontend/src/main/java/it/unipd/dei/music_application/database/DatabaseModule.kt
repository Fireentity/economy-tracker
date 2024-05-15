package it.unipd.dei.music_application.database

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.unipd.dei.music_application.daos.CategoryDao
import it.unipd.dei.music_application.daos.MovementDao
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
        ).build();
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