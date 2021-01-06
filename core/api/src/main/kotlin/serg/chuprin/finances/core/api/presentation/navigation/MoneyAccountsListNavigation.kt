package serg.chuprin.finances.core.api.presentation.navigation

import android.view.View
import androidx.navigation.NavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import serg.chuprin.finances.core.api.presentation.screen.arguments.MoneyAccountDetailsScreenArguments

/**
 * Created by Sergey Chuprin on 07.05.2020.
 */
interface MoneyAccountsListNavigation {

    fun navigateToMoneyAccountCreation(
        navController: NavController,
        vararg sharedElementView: FloatingActionButton
    )

    fun navigateToMoneyAccountDetails(
        navController: NavController,
        screenArguments: MoneyAccountDetailsScreenArguments,
        vararg sharedElementViews: View
    )

}