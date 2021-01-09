package serg.chuprin.finances.feature.transactions.domain.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import serg.chuprin.finances.core.api.domain.linker.TransactionWithCategoriesLinker
import serg.chuprin.finances.core.api.domain.model.category.Category
import serg.chuprin.finances.core.api.domain.model.category.CategoryWithParentForId
import serg.chuprin.finances.core.api.domain.model.period.DataPeriodType
import serg.chuprin.finances.core.api.domain.model.transaction.Transaction
import serg.chuprin.finances.core.api.extensions.flow.distinctUntilChangedBy
import serg.chuprin.finances.feature.transactions.domain.model.ReportDataPeriod
import serg.chuprin.finances.feature.transactions.domain.model.TransactionReportCurrentPeriodData
import serg.chuprin.finances.feature.transactions.domain.model.TransactionReportFilter
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 25.12.2020.
 *
 * Observes data for current data period from [TransactionReportFilter].
 * The data is: transactions, category amount shares.
 */
class TransactionReportCurrentPeriodDataService @Inject constructor(
    private val transactionWithCategoriesLinker: TransactionWithCategoriesLinker
) {

    private companion object {

        /**
         * We want to rebuild list data if report period type has changed
         * i.e from [ReportDataPeriod.Predefined] to [ReportDataPeriod.Custom]
         * or from [DataPeriodType.DAY] to [DataPeriodType.MONTH].
         *
         * @see [buildData]
         */
        private val INTERESTED_KEYS: List<((TransactionReportFilter) -> Any?)> = listOf(
            TransactionReportFilter::reportDataPeriod
        )

    }

    fun dataFlow(
        transactionsFlow: Flow<List<Transaction>>,
        categoriesFlow: Flow<CategoryWithParentForId>,
        filterFlow: Flow<TransactionReportFilter>
    ): Flow<TransactionReportCurrentPeriodData> {
        return combine(
            transactionsFlow,
            categoriesFlow,
            filterFlow.distinctUntilChangedBy(INTERESTED_KEYS),
            ::buildData
        )
    }

    private fun buildData(
        transactions: List<Transaction>,
        categoryWithParentForId: CategoryWithParentForId,
        filter: TransactionReportFilter
    ): TransactionReportCurrentPeriodData {
        val transactionsInCurrentPeriod = transactions.filter { transaction ->
            transaction.dateTime in filter.reportDataPeriod
        }
        val transactionCategories = transactionWithCategoriesLinker.linkTransactionsWithCategories(
            transactionsInCurrentPeriod,
            categoryWithParentForId
        )
        return TransactionReportCurrentPeriodData(
            transactionCategories = transactionCategories,
            categoryTransactions = buildCategoryTransactions(
                filter = filter,
                categoryWithParentForId = categoryWithParentForId,
                transactionsInCurrentPeriod = transactionsInCurrentPeriod
            )
        )
    }

    private fun buildCategoryTransactions(
        categoryWithParentForId: CategoryWithParentForId,
        filter: TransactionReportFilter,
        transactionsInCurrentPeriod: List<Transaction>
    ): Map<Category?, List<Transaction>> {

        fun link(): Map<Category?, List<Transaction>> {
            return transactionWithCategoriesLinker.linkCategoryParentsWithTransactions(
                transactionsInCurrentPeriod,
                categoryWithParentForId
            )
        }

        return when (filter) {
            is TransactionReportFilter.Categories -> link()
            is TransactionReportFilter.SingleCategory -> emptyMap()
            is TransactionReportFilter.Plain -> {
                if (filter.transactionType == null) {
                    emptyMap()
                } else {
                    link()
                }
            }
        }
    }

}