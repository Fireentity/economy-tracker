package it.unipd.dei.common_backend.models

import java.util.UUID

class MovementBuilder(
    var amount: Double? = null,
    var category: Category? = null,
    var date: Long? = null,
    var createdAt: Long? = null
) {

    fun toMovement(): Movement? {

        if (amount == null || category == null || date == null) {
            return null
        }

        val createdAt = this.createdAt ?: System.currentTimeMillis()

        return Movement(
            //TODO fix this with epoch uuids
            UUID.randomUUID(),
            amount!!,
            category!!.uuid,
            date!!,
            createdAt,
            System.currentTimeMillis()
        )
    }
}
