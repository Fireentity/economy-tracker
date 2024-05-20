package it.unipd.dei.music_application.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "movements",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["uuid"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Movement(
    @PrimaryKey
    val uuid: UUID,
    val amount: Double,
    val categoryId: UUID,
    val createdAt: Long,
    val updatedAt: Long
)
