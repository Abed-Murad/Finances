package serg.chuprin.finances.feature.categories.impl.presentation.model.cell

import serg.chuprin.finances.core.api.domain.model.Id
import serg.chuprin.finances.core.api.domain.model.category.TransactionCategory
import serg.chuprin.finances.core.api.presentation.model.cells.DiffCell

/**
 * Created by Sergey Chuprin on 28.12.2020.
 */
interface CategoryCell : DiffCell<Id> {

    val category: TransactionCategory

    override val diffCellId: Id
        get() = category.id

}