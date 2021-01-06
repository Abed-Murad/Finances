package serg.chuprin.finances.feature.transactions.presentation.model.store

import serg.chuprin.finances.core.api.presentation.model.cells.BaseCell
import serg.chuprin.finances.feature.transactions.domain.model.TransactionReportFilter
import serg.chuprin.finances.feature.transactions.presentation.model.TransactionReportHeader
import serg.chuprin.finances.feature.transactions.presentation.model.cells.TransactionReportCategorySharesCell

/**
 * Created by Sergey Chuprin on 12.05.2020.
 */
data class TransactionsReportState(
    val transactionListCells: List<BaseCell> = emptyList(),
    val header: TransactionReportHeader = TransactionReportHeader(),
    // TODO: Refactor cells organization.
    val categorySharesCell: TransactionReportCategorySharesCell? = null,
    val filter: TransactionReportFilter = TransactionReportFilter.UNINITIALIZED
)