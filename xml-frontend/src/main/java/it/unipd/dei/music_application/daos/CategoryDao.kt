package it.unipd.dei.music_application.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.Movement

@Dao
interface CategoryDao {
    @Upsert
    suspend fun insertMovement(movement: Movement)

    @Delete
    suspend fun deleteMovement(movement: Movement)

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<Category>
}