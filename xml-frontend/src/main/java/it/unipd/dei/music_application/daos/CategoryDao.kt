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
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<Category>
}