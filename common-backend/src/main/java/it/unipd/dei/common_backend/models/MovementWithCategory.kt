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
) {
    fun upsertIfPositive(list: MutableList<MovementWithCategory>) {
        if(movement.amount <= 0) {
            return
        }

        val index = list.binarySearchBy(this.movement.date) {
            it.movement.date
        }

        if(index < 0) {
            list.add(-index+1, this)
        } else {
            list[index] = this
        }
    }

    fun upsertIfNegative(list: MutableList<MovementWithCategory>) {
        if(movement.amount >= 0) {
            return
        }

        val index = list.binarySearchBy(this.movement.date) {
            it.movement.date
        }

        if(index < 0) {
            list.add(-index+1, this)
        } else {
            list[index] = this
        }
    }

    fun upsert(list: MutableList<MovementWithCategory>) {
        val index = list.binarySearchBy(this.movement.date) {
            it.movement.date
        }

        if(index < 0) {
            list.add(-index+1, this)
        } else {
            list[index] = this
        }
    }

    fun deleteIfPositive(list: MutableList<MovementWithCategory>) {
        if(movement.amount <= 0) {
            return
        }

        val index = list.binarySearchBy(this.movement.date) {
            it.movement.date
        }

        if(index >= 0) {
            list.removeAt(index)
        }
    }

    fun deleteIfNegative(list: MutableList<MovementWithCategory>) {
        if(movement.amount >= 0) {
            return
        }

        val index = list.binarySearchBy(this.movement.date) {
            it.movement.date
        }

        if(index >= 0) {
            list.removeAt(index)
        }
    }

    fun delete(list: MutableList<MovementWithCategory>) {
        val index = list.binarySearchBy(this.movement.date) {
            it.movement.date
        }

        if(index >= 0) {
            list.removeAt(index)
        }
    }
}