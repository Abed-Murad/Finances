package serg.chuprin.finances.feature.categories.impl.presentation.model.cell

import serg.chuprin.finances.core.api.domain.model.category.TransactionCategory

/**
 * Created by Sergey Chuprin on 28.12.2020.
 */
data class ParentCategoryCell(
    val color: Int,
    val isExpansionAvailable: Boolean,
    override val category: TransactionCategory
) : CategoryCell