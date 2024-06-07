package it.unipd.dei.common_backend.models

import java.util.UUID

class MovementBuilder(var amount: Double? = null, var category: Category? = null) {

    fun toMovement(): Movement? {
        return amount?.let { amount ->
            category?.let { category ->
                Movement(
                    //TODO fix this with epoch uuids
                    UUID.randomUUID(),
                    amount,
                    category.uuid,
                    System.currentTimeMillis(),
                    System.currentTimeMillis()
                )
            }
        }
    }
}
