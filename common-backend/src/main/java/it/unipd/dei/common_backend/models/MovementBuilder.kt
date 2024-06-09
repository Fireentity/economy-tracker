package it.unipd.dei.common_backend.models

import java.util.UUID

class MovementBuilder(
    val amount: () -> Double? = { null },
    val category: () -> Category? = { null },
    val date: () -> Long? = { null },
    val movement: Movement? = null
) {

    fun toMovement(): Movement? {

        val amount = amount()
        val category = category()
        val date = date()

        if (amount == null || category == null || date == null) {
            return null
        }

        val createdAt = movement?.createdAt ?: System.currentTimeMillis()
        val uuid = movement?.uuid ?: UUID.randomUUID()
        return Movement(
            //TODO fix this with epoch uuids
            uuid,
            amount,
            category.uuid,
            date,
            createdAt,
            System.currentTimeMillis()
        )
    }
}
