package serg.chuprin.finances.feature.moneyaccount.creation.presentation.model.store

/**
 * Created by Sergey Chuprin on 01.06.2020.
 */
sealed class MoneyAccountCreationAction {

    class ExecuteIntent(
        val intent: MoneyAccountCreationIntent
    ) : MoneyAccountCreationAction()

}