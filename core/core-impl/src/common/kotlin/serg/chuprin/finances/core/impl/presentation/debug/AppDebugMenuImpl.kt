package serg.chuprin.finances.core.impl.presentation.debug

import android.app.Application
import com.github.ajalt.timberkt.Timber
import com.pandulapeter.beagle.Beagle
import com.pandulapeter.beagleCore.configuration.Appearance
import com.pandulapeter.beagleCore.configuration.Trick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import serg.chuprin.finances.core.api.domain.model.Id
import serg.chuprin.finances.core.api.domain.model.MoneyAccount
import serg.chuprin.finances.core.api.domain.model.User
import serg.chuprin.finances.core.api.domain.model.category.TransactionCategoryType
import serg.chuprin.finances.core.api.domain.model.category.TransactionCategoryWithParent
import serg.chuprin.finances.core.api.domain.model.transaction.Transaction
import serg.chuprin.finances.core.api.domain.model.transaction.TransactionType
import serg.chuprin.finances.core.api.domain.repository.MoneyAccountRepository
import serg.chuprin.finances.core.api.domain.repository.TransactionCategoryRepository
import serg.chuprin.finances.core.api.domain.repository.TransactionRepository
import serg.chuprin.finances.core.api.domain.repository.UserRepository
import serg.chuprin.finances.core.api.presentation.model.manager.ResourceManger
import serg.chuprin.finances.core.impl.BuildConfig
import serg.chuprin.finances.core.impl.R
import serg.chuprin.finances.core.impl.di.initializer.AppDebugMenuInitializer
import java.util.concurrent.ThreadLocalRandom
import javax.inject.Inject
import serg.chuprin.finances.core.api.R as CoreR

/**
 * Created by Sergey Chuprin on 27.04.2020.
 */
internal class AppDebugMenuImpl @Inject constructor(
    private val resourceManger: ResourceManger,
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository,
    private val moneyAccountRepository: MoneyAccountRepository,
    private val transactionCategoryRepository: TransactionCategoryRepository
) : AppDebugMenuInitializer {

    private companion object {

        private val moneyAccountNames = listOf(
            "Debit card",
            "Debts",
            "Investition",
            "Bank",
            "Business",
            "Cash",
            "Personal",
            "Wife",
            "Family",
            "Broker",
            "Deposit",
            "Loan",
            "Tinkoff",
            "Sberbank",
            "Tochka bank",
            "Modulbank",
            "Alfa Bank",
            "Checking"
        )

    }

    override fun initialize(application: Application) {
        Timber.d { "Debug menu is initialized" }
        with(Beagle) {
            imprint(application, Appearance(R.style.DebugMenuTheme))
            learn(
                Trick.Header(
                    title = resourceManger.getString(CoreR.string.app_name),
                    subtitle = "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
                ),
                Trick.KeylineOverlayToggle(),
                Trick.ViewBoundsOverlayToggle(),
                Trick.Padding(),
                Trick.Text(text = "Data generation"),
                Trick.Button(
                    text = "Create income transaction",
                    onButtonPressed = {
                        launch { createTransaction(TransactionCategoryType.INCOME) }
                    }
                ),
                Trick.Button(
                    text = "Create expense transaction",
                    onButtonPressed = {
                        launch { createTransaction(TransactionCategoryType.EXPENSE) }
                    }
                ),
                Trick.Button(
                    text = "Create money account",
                    onButtonPressed = {
                        launch { createMoneyAccount() }
                    }
                )
            )
        }
    }

    private suspend fun createTransaction(categoryType: TransactionCategoryType) {
        val currentUser = userRepository.getCurrentUser()

        val categoryWithParent = getRandomCategory(currentUser, categoryType)
        val moneyAccount = getRandomMoneyAccount(currentUser)

        val amount = ThreadLocalRandom.current().nextInt(100, 2000).run {
            when (categoryType) {
                TransactionCategoryType.INCOME -> this
                TransactionCategoryType.EXPENSE -> this * -1
            }
        }

        val transaction = Transaction(
            id = Id.createNew(),
            ownerId = currentUser.id,
            _amount = amount.toString(),
            type = TransactionType.PLAIN,
            moneyAccountId = moneyAccount.id,
            currencyCode = moneyAccount.currencyCode,
            categoryId = categoryWithParent?.category?.id
        )
        transactionRepository.createTransaction(transaction)
        Timber.d { "New test transaction created: $transaction" }
    }

    private suspend fun createMoneyAccount() {
        val currentUser = userRepository.getCurrentUser()
        val moneyAccount = MoneyAccount(
            id = Id.createNew(),
            ownerId = currentUser.id,
            name = moneyAccountNames.shuffled().first(),
            currencyCode = currentUser.defaultCurrencyCode,
            isFavorite = ThreadLocalRandom.current().nextBoolean()
        )
        moneyAccountRepository.createAccount(moneyAccount)
        Timber.d { "New test money account created: $moneyAccount" }
    }

    private fun launch(block: suspend () -> Unit) {
        GlobalScope.launch {
            withContext(Dispatchers.Default) {
                block()
            }
        }
    }

    private suspend fun getRandomMoneyAccount(currentUser: User): MoneyAccount {
        return moneyAccountRepository.getUserAccounts(currentUser.id).shuffled().first()
    }

    private suspend fun getRandomCategory(
        currentUser: User,
        type: TransactionCategoryType
    ): TransactionCategoryWithParent? {
        return if (ThreadLocalRandom.current().nextBoolean()) {
            transactionCategoryRepository
                .getUserCategories(currentUser.id, type)
                .entries
                .shuffled()
                .first()
                .value
        } else {
            null
        }
    }

}