package serg.chuprin.finances.feature.transaction.presentation.model.store

import serg.chuprin.finances.core.api.domain.model.category.TransactionCategory
import java.time.LocalDate

/**
 * Created by Sergey Chuprin on 02.01.2021.
 */
sealed class TransactionAction {

    class ExecuteIntent(
        val intent: TransactionIntent
    ) : TransactionAction()

    class FormatInitialState(
        val date: LocalDate,
        val category: TransactionCategory?
    ) : TransactionAction()

}