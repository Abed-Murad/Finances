package serg.chuprin.finances.feature.transactions.presentation.model.store

import serg.chuprin.finances.feature.transactions.presentation.model.cells.TransactionReportDataPeriodAmountChartCell

/**
 * Created by Sergey Chuprin on 12.05.2020.
 */
sealed class TransactionsReportIntent {

    class ClickOnDataChartCell(
        val dataPeriodAmountChartCell: TransactionReportDataPeriodAmountChartCell
    ) : TransactionsReportIntent()

}