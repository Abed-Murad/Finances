package serg.chuprin.finances.feature.onboarding.domain

import serg.chuprin.finances.core.api.domain.model.MoneyAccountBalance

/**
 * Created by Sergey Chuprin on 13.04.2020.
 */
class OnboardingMoneyAccountCreationParams(
    val accountName: String,
    val balance: MoneyAccountBalance
)