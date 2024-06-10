package it.unipd.dei.common_backend.models

import it.unipd.dei.common_backend.utils.DateHelper
import it.unipd.dei.common_backend.viewModels.CategoryViewModel
import java.util.UUID

class MovementBuilder(
    val amount: () -> String = { "" },
    val category: () -> String = { "" },
    val date: () -> String = { "" },
    val movement: Movement? = null
) {

    fun toMovement(categoryViewModel: CategoryViewModel): MovementWithCategory? {

        val createdAt = movement?.createdAt ?: System.currentTimeMillis()
        val uuid = movement?.uuid ?: UUID.randomUUID()
        return categoryViewModel.getCategoryByIdentifier(category())?.let { category ->
            DateHelper.convertFromDateTimeToMilliseconds(date())?.let { date ->
                amount().toDoubleOrNull()?.let { amount ->
                    MovementWithCategory(
                        Movement(
                            //TODO fix this with epoch uuids
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
        }
    }
}
