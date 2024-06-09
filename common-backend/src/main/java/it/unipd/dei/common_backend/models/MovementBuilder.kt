package it.unipd.dei.common_backend.models

import it.unipd.dei.common_backend.utils.DateHelper
import it.unipd.dei.common_backend.view.CategoryViewModel
import java.util.UUID

class MovementBuilder(
    val amount: () -> String = { "" },
    val category: () -> String = { "" },
    val date: () -> String = { "" },
    val movement: Movement? = null
) {

    fun toMovement(categoryViewModel: CategoryViewModel): Movement? {

        val amount = amount()
        val category = category()
        val date = date()

        val createdAt = movement?.createdAt ?: System.currentTimeMillis()
        val uuid = movement?.uuid ?: UUID.randomUUID()
        return Movement(
            //TODO fix this with epoch uuids
            uuid,
            amount.toDouble(),
            categoryViewModel.getCategoryByIdentifier(category)!!.uuid,
            DateHelper.convertFromDateTimeToMilliseconds(date)!!,
            createdAt,
            System.currentTimeMillis()
        )
    }
}
