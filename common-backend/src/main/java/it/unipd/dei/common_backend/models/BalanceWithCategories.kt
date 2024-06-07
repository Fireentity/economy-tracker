package it.unipd.dei.common_backend.models

import androidx.room.Embedded
import androidx.room.Relation

data class BalanceWithCategories(
    @Embedded
    val balance: Balance,
    @Relation(
        parentColumn = "uuid",
        entityColumn = "movementId"
    )
    val movements: List<Movement>
)