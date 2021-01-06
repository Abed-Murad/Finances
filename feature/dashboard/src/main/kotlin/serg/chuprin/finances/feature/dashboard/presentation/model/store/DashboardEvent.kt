package serg.chuprin.finances.feature.dashboard.presentation.model.store

import serg.chuprin.finances.core.api.presentation.model.cells.DataPeriodTypePopupMenuCell
import serg.chuprin.finances.core.api.presentation.screen.arguments.MoneyAccountDetailsScreenArguments
import serg.chuprin.finances.core.api.presentation.screen.arguments.MoneyAccountsListScreenArguments
import serg.chuprin.finances.core.api.presentation.screen.arguments.TransactionScreenArguments
import serg.chuprin.finances.core.api.presentation.screen.arguments.TransactionsReportScreenArguments

/**
 * Created by Sergey Chuprin on 16.04.2020.
 */
sealed class DashboardEvent {

    class ShowPeriodTypesPopupMenu(
        val menuCells: List<DataPeriodTypePopupMenuCell>
    ) : DashboardEvent()

    class NavigateToMoneyAccountDetailsScreen(
        val screenArguments: MoneyAccountDetailsScreenArguments
    ) : DashboardEvent()

    class NavigateToTransactionsReportScreen(
        val arguments: TransactionsReportScreenArguments
    ) : DashboardEvent()

    class NavigateToTransactionScreen(
        val screenArguments: TransactionScreenArguments
    ) : DashboardEvent()

    class NavigateToMoneyAccountsListScreen(
        val screenArguments: MoneyAccountsListScreenArguments
    ) : DashboardEvent()

    object NavigateToMoneyAccountCreationScreen : DashboardEvent()

}