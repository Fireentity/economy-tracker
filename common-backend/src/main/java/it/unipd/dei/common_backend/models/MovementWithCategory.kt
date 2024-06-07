package it.unipd.dei.common_backend.models

import androidx.room.Embedded
import androidx.room.Relation

data class MovementWithCategory(
    @Embedded
    val movement: Movement,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "uuid"
    )
    val category: Category
)