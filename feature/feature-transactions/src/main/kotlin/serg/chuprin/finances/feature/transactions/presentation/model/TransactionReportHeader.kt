package serg.chuprin.finances.feature.transactions.presentation.model

import serg.chuprin.finances.core.api.extensions.EMPTY_STRING
import serg.chuprin.finances.feature.transactions.presentation.model.cells.TransactionReportChartListCell

/**
 * Created by Sergey Chuprin on 13.12.2020.
 */
data class TransactionReportHeader(
    val title: String = EMPTY_STRING,
    val subtitle: String = EMPTY_STRING,
    val chartListCell: List<TransactionReportChartListCell> = emptyList()
)