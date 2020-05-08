package serg.chuprin.finances.core.api.domain.model

import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature
import strikt.api.expectThat
import strikt.assertions.containsKey
import strikt.assertions.isNotSameInstanceAs
import strikt.assertions.isSorted
import java.math.BigDecimal


/**
 * Created by Sergey Chuprin on 26.04.2020.
 */
object MoneyAccountBalancesTest : Spek({

    Feature("Dashboard money accounts") {

        Scenario("Adding new account") {

            val originalAccounts =
                MoneyAccountBalances()

            lateinit var accountAmounts1: MoneyAccountBalances
            val firstAccount =
                createAccount(
                    "account",
                    true
                )

            When("New account is added to list") {
                accountAmounts1 = originalAccounts.add(firstAccount, BigDecimal.ONE)
            }

            Then("Original accounts does not contain this account") {
                expectThat(originalAccounts).not().containsKey(firstAccount)
            }

            And("New accounts contains added account") {
                expectThat(accountAmounts1).containsKey(firstAccount)
            }

            And("Original accounts is not same instance as new accounts") {
                expectThat(originalAccounts).isNotSameInstanceAs(accountAmounts1)
            }

        }

        Scenario("Preserving sort order after new accounts adding") {

            lateinit var accountBalances: MoneyAccountBalances

            When("Accounts are added") {
                accountBalances = MoneyAccountBalances()
                    .add(
                        createAccount(
                            "a",
                            isFavorite = true
                        ), BigDecimal.ONE
                    )
                    .add(
                        createAccount(
                            "b",
                            isFavorite = false
                        ), BigDecimal.ONE
                    )
                    .add(
                        createAccount(
                            "c",
                            isFavorite = false
                        ), BigDecimal.ONE
                    )
                    .add(
                        createAccount(
                            "d",
                            isFavorite = true
                        ), BigDecimal.ONE
                    )
            }

            Then("Keys are properly sorted") {
                expectThat(accountBalances)
                    .get { keys }
                    .isSorted(MoneyAccountBalances.accountsComparator)
            }

        }

    }

})

private fun createAccount(name: String, isFavorite: Boolean): MoneyAccount {
    return MoneyAccount(
        Id.createNew(),
        name,
        Id.createNew(),
        isFavorite,
        "rub"
    )
}