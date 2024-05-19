package it.unipd.dei.music_application.models

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