package serg.chuprin.finances.feature.onboarding.domain.usecase

import serg.chuprin.finances.core.api.domain.model.OnboardingStep
import serg.chuprin.finances.core.api.domain.repository.OnboardingRepository
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 08.04.2020.
 */
class CompleteCurrencyChoiceOnboardingUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) {

    fun execute() {
        onboardingRepository.onboardingStep = OnboardingStep.ACCOUNT_SETUP
    }

}