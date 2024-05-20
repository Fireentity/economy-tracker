package it.unipd.dei.music_application.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val uuid: UUID,
    val identifier: String,
    val createdAt: Long,
    val updatedAt: Long
)