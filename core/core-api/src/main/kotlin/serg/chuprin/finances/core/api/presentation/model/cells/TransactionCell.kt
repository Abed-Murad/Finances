package serg.chuprin.finances.core.api.presentation.model.cells

import androidx.annotation.ColorInt
import serg.chuprin.finances.core.api.domain.model.Id
import serg.chuprin.finances.core.api.domain.model.transaction.Transaction

/**
 * Created by Sergey Chuprin on 07.05.2020.
 */
data class TransactionCell(
    val amount: String,
    @ColorInt
    val color: Int,
    val time: String,
    val isIncome: Boolean,
    val subcategoryName: String,
    val parentCategoryName: String,
    val transaction: Transaction,
    val transitionName: String
) : DiffCell<Id> {

    override val diffCellId: Id = transaction.id

}