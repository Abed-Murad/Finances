package serg.chuprin.finances.core.impl.domain.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import serg.chuprin.finances.core.api.domain.model.TransactionCategoriesMap
import serg.chuprin.finances.core.api.domain.model.category.TransactionCategory
import serg.chuprin.finances.core.api.domain.model.transaction.Transaction
import serg.chuprin.finances.core.api.domain.model.transaction.TransactionsQuery
import serg.chuprin.finances.core.api.domain.repository.TransactionCategoryRepository
import serg.chuprin.finances.core.api.domain.repository.TransactionRepository
import serg.chuprin.finances.core.api.domain.service.TransactionCategoryRetrieverService
import serg.chuprin.finances.core.api.extensions.categoryIds
import serg.chuprin.finances.core.impl.data.TransactionWithCategoriesLinker
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 20.04.2020.
 */
internal class TransactionCategoryRetrieverServiceImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: TransactionCategoryRepository,
    private val transactionWithCategoriesLinker: TransactionWithCategoriesLinker
) : TransactionCategoryRetrieverService {

    override fun transactionsFlow(query: TransactionsQuery): Flow<TransactionCategoriesMap> {
        return transactionRepository
            .transactionsFlow(query)
            .flatMapLatest { transactions ->
                combine(
                    flowOf(transactions),
                    categoryRepository.categoriesFlow(transactions.categoryIds)
                ) { t1, t2 ->
                    transactionWithCategoriesLinker.linkTransactionsWithCategories(t1, t2)
                }
            }
    }

    override fun categoryTransactionsFlow(
        query: TransactionsQuery
    ): Flow<Map<TransactionCategory?, List<Transaction>>> {
        return transactionRepository
            .transactionsFlow(query)
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