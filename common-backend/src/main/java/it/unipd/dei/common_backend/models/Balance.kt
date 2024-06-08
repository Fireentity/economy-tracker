package it.unipd.dei.common_backend.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "balances")
data class Balance(
    @PrimaryKey
    val uuid: UUID,
    val identifier: String,
    val createdAt: Long,
    val updatedAt: Long
)