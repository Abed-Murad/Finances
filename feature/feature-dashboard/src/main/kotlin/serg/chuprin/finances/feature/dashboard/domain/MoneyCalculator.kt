package serg.chuprin.finances.feature.dashboard.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import serg.chuprin.finances.core.api.domain.model.DataPeriod
import serg.chuprin.finances.core.api.domain.model.Id
import serg.chuprin.finances.core.api.domain.model.Transaction
import serg.chuprin.finances.core.api.domain.model.TransactionType
import serg.chuprin.finances.core.api.domain.repository.TransactionRepository
import java.math.BigDecimal
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 17.04.2020.
 */
class MoneyCalculator @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    fun calculateBalance(currentUserId: Id): Flow<BigDecimal> {
        return performCalculation(currentUserId, dataPeriod = null)
    }

    fun calculateExpenses(currentUserId: Id, dataPeriod: DataPeriod): Flow<BigDecimal> {
        return performCalculation(
            currentUserId,
            dataPeriod = dataPeriod,
            transactionFilter = { transaction ->
                transaction.isExpense && transaction.type == TransactionType.PLAIN
            }
        ).map { bigDecimal -> bigDecimal.abs() }
    }

    fun calculateIncomes(currentUserId: Id, dataPeriod: DataPeriod): Flow<BigDecimal> {
        return performCalculation(
            currentUserId,
            dataPeriod = dataPeriod,
            transactionFilter = { transaction ->
                transaction.isIncome && transaction.type == TransactionType.PLAIN
            }
        ).map { bigDecimal -> bigDecimal.abs() }
    }

    private fun performCalculation(
        currentUserId: Id,
        dataPeriod: DataPeriod?,
        transactionFilter: ((Transaction) -> Boolean)? = null
    ): Flow<BigDecimal> {
        return flow {
            val userTransactionsFlow = if (dataPeriod == null) {
                transactionRepository.userTransactionsFlow(currentUserId)
            } else {
                transactionRepository.userTransactionsFlow(currentUserId, dataPeriod)
            }
            val flow = userTransactionsFlow
                .map { transactions ->
                    transactions.fold(
                        BigDecimal.ZERO,
                        { balance, transaction ->
                            if (transactionFilter == null || transactionFilter(transaction)) {
                                balance.plus(transaction.amount)
                            } else {
                                balance
                            }
                        }
                    )
                }
            emitAll(flow)
        }
    }

}