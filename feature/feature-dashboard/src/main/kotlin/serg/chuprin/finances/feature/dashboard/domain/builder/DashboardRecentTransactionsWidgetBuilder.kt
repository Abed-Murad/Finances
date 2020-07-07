package serg.chuprin.finances.feature.dashboard.domain.builder

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import serg.chuprin.finances.core.api.domain.model.User
import serg.chuprin.finances.core.api.domain.model.period.DataPeriod
import serg.chuprin.finances.core.api.domain.model.transaction.TransactionsQuery
import serg.chuprin.finances.core.api.domain.service.TransactionCategoryRetrieverService
import serg.chuprin.finances.feature.dashboard.domain.model.DashboardWidget
import serg.chuprin.finances.feature.dashboard.domain.repository.DashboardDataPeriodRepository
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 20.04.2020.
 */
class DashboardRecentTransactionsWidgetBuilder @Inject constructor(
    private val dataPeriodRepository: DashboardDataPeriodRepository,
    private val transactionCategoryRetrieverService: TransactionCategoryRetrieverService
) : DashboardWidgetBuilder<DashboardWidget.RecentTransactions> {

    private companion object {
        private const val RECENT_TRANSACTIONS_COUNT = 4
    }

    override fun build(
        currentUser: User,
        currentPeriod: DataPeriod
    ): Flow<DashboardWidget.RecentTransactions>? {
        if (dataPeriodRepository.defaultDataPeriod != currentPeriod) {
            return null
        }
        return transactionCategoryRetrieverService
            .transactionsFlow(
                TransactionsQuery(
                    userId = currentUser.id,
                    endDate = currentPeriod.endDate,
                    limit = RECENT_TRANSACTIONS_COUNT,
                    startDate = currentPeriod.startDate
                )
            )
            .map { map -> DashboardWidget.RecentTransactions(currentUser.defaultCurrency, map) }
    }

}