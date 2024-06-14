package it.unipd.dei.common_backend.models

import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import java.util.UUID

class MovementBuilder(
    val amount: () -> String = { "" },
    val category: () -> String = { "" },
    val date: () -> Long? = { null },
    val movement: Movement? = null
) {

    fun toMovement(categoryViewModel: CategoryViewModel): MovementWithCategory? {
        val category = categoryViewModel.getCategoryByIdentifier(category()) ?: return null
        val date = date() ?: return null
        val amount = amount().toDoubleOrNull() ?: return null
        if (amount == 0.0) return null


        val createdAt = movement?.createdAt ?: System.currentTimeMillis()
        val uuid = movement?.uuid ?: UUID.randomUUID()

        return MovementWithCategory(
            Movement(
                uuid,
                amount,
                category.uuid,
                date,
                createdAt,
                System.currentTimeMillis()
            ),
            category
        )
    }
}
