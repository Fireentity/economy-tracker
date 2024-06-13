package it.unipd.dei.common_backend.models

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
) {

    fun withUpdatedAt(updatedAt: Long): Category {
        return Category(uuid, identifier, createdAt, updatedAt);
    }

    fun withCategoryIdentifier(identifier: String): Category {
        return Category(uuid, identifier, createdAt, updatedAt);
    }

    override fun toString(): String {
        return identifier
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Category) return false
        return uuid == other.uuid || identifier == other.identifier
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + identifier.hashCode()
        return result
    }
}