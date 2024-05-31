package it.unipd.dei.music_application.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import it.unipd.dei.music_application.models.Category
import it.unipd.dei.music_application.models.CategoryTotal

@Dao
interface CategoryDao {
    @Upsert
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>

    @Transaction
    @Query("SELECT categories.identifier, SUM(amount) as totalAmount FROM movements JOIN categories ON movements.categoryId = categories.uuid GROUP BY categoryId")
    suspend fun getTotalAmountByCategory(): List<CategoryTotal>
    @Transaction
    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Transaction
    @Query("SELECT * FROM categories WHERE identifier = :identifier")
    suspend fun getCategoriesByIdentifier(identifier: String): List<Category>
}
