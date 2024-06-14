package it.unipd.dei.common_backend.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.models.CategoryTotal

@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Query("SELECT * FROM categories ORDER BY identifier ASC")
    suspend fun getAllCategories(): List<Category>

    @Transaction
    @Query("SELECT categories.identifier, SUM(amount) as totalAmount FROM movements JOIN categories ON movements.categoryId = categories.uuid GROUP BY categoryId")
    suspend fun getTotalAmountByCategory(): List<CategoryTotal>

    @Transaction
    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()
}
