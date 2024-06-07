package it.unipd.dei.common_backend.models

import androidx.room.Embedded
import androidx.room.Relation
import it.unipd.dei.common_backend.models.Category
import it.unipd.dei.common_backend.models.Movement

data class MovementWithCategory(
    @Embedded
    val movement: Movement,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "uuid"
    )
    val category: Category
)