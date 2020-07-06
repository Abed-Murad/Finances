package serg.chuprin.finances.core.impl.domain.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import serg.chuprin.finances.core.api.domain.model.Id
import serg.chuprin.finances.core.api.domain.model.TransactionCategoriesMap
import serg.chuprin.finances.core.api.domain.model.category.TransactionCategory
import serg.chuprin.finances.core.api.domain.model.category.TransactionCategoryWithParent
import serg.chuprin.finances.core.api.domain.model.period.DataPeriod
import serg.chuprin.finances.core.api.domain.model.transaction.PlainTransactionType
import serg.chuprin.finances.core.api.domain.model.transaction.Transaction
import serg.chuprin.finances.core.api.domain.repository.TransactionCategoryRepository
import serg.chuprin.finances.core.api.domain.repository.TransactionRepository
import serg.chuprin.finances.core.api.domain.service.TransactionCategoryRetrieverService
import serg.chuprin.finances.core.api.extensions.categoryIds
import serg.chuprin.finances.core.impl.data.TransactionWithCategoriesLinker
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 20.04.2020.
 */
internal class TransactionCategoryRetrieverServiceImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: TransactionCategoryRepository,
    private val transactionWithCategoriesLinker: TransactionWithCategoriesLinker
) : TransactionCategoryRetrieverService {

    override fun moneyAccountTransactionsFlow(moneyAccountId: Id): Flow<TransactionCategoriesMap> {
        return transactionRepository
            .moneyAccountTransactionsFlow(moneyAccountId)
            .flatMapLatest { transactions ->
                combine(
                    flowOf(transactions),
                    categoryRepository.categoriesFlow(transactions.categoryIds)
                ) { t1, t2 ->
                    transactionWithCategoriesLinker.linkTransactionsWithCategories(t1, t2)
                }
            }
    }

    override fun recentUserTransactionsInPeriodFlow(
        userId: Id,
        count: Int,
        dataPeriod: DataPeriod
    ): Flow<TransactionCategoriesMap> {
        return transactionRepository
            .recentUserTransactionsFlow(userId, count, dataPeriod)
            .flatMapLatest { transactions ->
                combine(
                    flowOf(transactions),
                    categoryRepository.categoriesFlow(transactions.categoryIds)
                ) { t1, t2 ->
                    transactionWithCategoriesLinker.linkTransactionsWithCategories(t1, t2)
                }
            }
    }

    override fun userTransactionsFlow(
        userId: Id,
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        includedCategoryIds: Set<Id>,
        transactionType: PlainTransactionType?
    ): Flow<Map<Transaction, TransactionCategoryWithParent?>> {
        return transactionRepository
            .userTransactionsFlow(
                userId = userId,
                startDate = startDate,
                endDate = endDate,
                includedCategoryIds = includedCategoryIds,
                transactionType = transactionType
            )
            .flatMapLatest { transactions ->
                combine(
                    flowOf(transactions),
                    categoryRepository.categoriesFlow(transactions.categoryIds)
                ) { t1, t2 ->
                    transactionWithCategoriesLinker.linkTransactionsWithCategories(t1, t2)
                }
            }
    }

    override fun userCategoryTransactionsInPeriod(
        userId: Id,
        dataPeriod: DataPeriod,
        transactionType: PlainTransactionType
    ): Flow<Map<TransactionCategory?, List<Transaction>>> {
        return transactionRepository
            .userTransactionsFlow(
                userId = userId,
                startDate = dataPeriod.startDate,
                endDate = dataPeriod.endDate,
                transactionType = transactionType
            )
            .flatMapLatest { transactions ->
                combine(
                    flowOf(transactions),
                    categoryRepository.categoriesFlow(transactions.categoryIds)
                ) { t1, t2 ->
                    transactionWithCategoriesLinker.linkCategoryParentsWithTransactions(t1, t2)
                }
            }
    }

}