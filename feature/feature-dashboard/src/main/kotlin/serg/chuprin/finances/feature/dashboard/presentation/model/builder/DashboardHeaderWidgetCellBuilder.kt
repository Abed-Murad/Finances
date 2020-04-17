package serg.chuprin.finances.feature.dashboard.presentation.model.builder

import serg.chuprin.finances.core.api.presentation.formatter.DataPeriodFormatter
import serg.chuprin.finances.core.api.presentation.model.formatter.AmountFormatter
import serg.chuprin.finances.feature.dashboard.domain.model.DashboardWidget
import serg.chuprin.finances.feature.dashboard.presentation.model.cells.DashboardWidgetCell
import java.math.BigDecimal
import java.util.*
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 17.04.2020.
 */
class DashboardHeaderWidgetCellBuilder @Inject constructor(
    private val amountFormatter: AmountFormatter,
    private val dataPeriodFormatter: DataPeriodFormatter
) : DashboardWidgetCellBuilder {

    override fun build(
        widget: DashboardWidget
    ): DashboardWidgetCell? {
        if (widget !is DashboardWidget.Header) {
            return null
        }
        return DashboardWidgetCell.DashboardHeaderCell(
            balance = formatAmount(widget.balance, widget.currency),
            incomesAmount = formatAmount(widget.currentPeriodIncomes, widget.currency),
            expensesAmount = formatAmount(widget.currentPeriodExpenses, widget.currency),
            currentPeriod = dataPeriodFormatter.formatAsCurrentPeriod(widget.dataPeriod)
        )
    }

    private fun formatAmount(amount: BigDecimal, currency: Currency): String {
        return amountFormatter.format(
            round = true,
            amount = amount,
            currency = currency,
            withCurrencySymbol = true
        )
    }
}