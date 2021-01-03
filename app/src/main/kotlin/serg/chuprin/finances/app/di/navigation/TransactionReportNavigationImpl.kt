package serg.chuprin.finances.app.di.navigation

import android.view.View
import androidx.navigation.NavController
import serg.chuprin.finances.core.api.presentation.model.TransactionReportNavigation
import serg.chuprin.finances.core.api.presentation.screen.arguments.TransactionScreenArguments
import serg.chuprin.finances.core.api.presentation.view.extensions.fragment.toBundle
import serg.chuprin.finances.feature.transaction.presentation.view.TransactionFragment
import serg.chuprin.finances.feature.transactions.presentation.view.TransactionsReportFragmentDirections.navigateFromTransactionReportToTransaction

/**
 * Created by Sergey Chuprin on 03.01.2021.
 */
class TransactionReportNavigationImpl : TransactionReportNavigation {

    override fun navigateToTransaction(
        navController: NavController,
        arguments: TransactionScreenArguments,
        sharedElementView: View
    ) {
        navController.navigate(
            navigateFromTransactionReportToTransaction().actionId,
            arguments.toBundle<TransactionFragment>(),
            null,
            sharedElementView.toNavigatorExtras()
        )
    }

}