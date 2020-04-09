package serg.chuprin.finances.feature.onboarding.presentation.currencychoice.view

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.activity.addCallback
import androidx.core.transition.doOnEnd
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_onboarding_currency_choice.*
import kotlinx.android.synthetic.main.view_currency_choice.view.*
import serg.chuprin.finances.core.api.presentation.model.viewmodel.extensions.viewModelFromComponent
import serg.chuprin.finances.core.api.presentation.view.BaseFragment
import serg.chuprin.finances.core.api.presentation.view.extensions.makeGone
import serg.chuprin.finances.core.api.presentation.view.extensions.makeVisible
import serg.chuprin.finances.core.api.presentation.view.extensions.onClick
import serg.chuprin.finances.core.api.presentation.view.extensions.showKeyboard
import serg.chuprin.finances.feature.onboarding.R
import serg.chuprin.finances.feature.onboarding.presentation.common.di.OnboardingFeatureComponent
import serg.chuprin.finances.feature.onboarding.presentation.common.view.OnboardingContainerFragment
import serg.chuprin.finances.feature.onboarding.presentation.currencychoice.model.store.CurrencyChoiceOnboardingEvent
import serg.chuprin.finances.feature.onboarding.presentation.currencychoice.model.store.CurrencyChoiceOnboardingIntent

/**
 * Created by Sergey Chuprin on 05.04.2020.
 */
class CurrencyChoiceOnboardingFragment :
    BaseFragment(R.layout.fragment_onboarding_currency_choice) {

    private val viewModel by viewModelFromComponent { parentComponent.currencyChoiceComponent() }

    private val parentComponent: OnboardingFeatureComponent
        get() = (parentFragment as OnboardingContainerFragment).component

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.dispatchIntent(CurrencyChoiceOnboardingIntent.BackPress)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currencyChoiceView.onCurrencyCellChosen = { cell ->
            viewModel.dispatchIntent(CurrencyChoiceOnboardingIntent.ChooseCurrency(cell))
        }
        currencyChoiceView.onCloseClicked = {
            viewModel.dispatchIntent(CurrencyChoiceOnboardingIntent.CloseCurrencyPicker)
        }
        textInputLayout.onClick {
            viewModel.dispatchIntent(CurrencyChoiceOnboardingIntent.ClickOnCurrencyPicker)
        }
        doneButton.onClick {
            viewModel.dispatchIntent(CurrencyChoiceOnboardingIntent.ClickOnDoneButton)
        }

        with(viewModel) {
            eventsLiveData(::handleEvent)
            doneButtonEnabledLiveData(doneButton::setEnabled)
            currencyCellsLiveData(currencyChoiceView::setCells)
            chosenCurrencyDisplayNameLiveData(textInputLayout::setText)
            currencyPickerVisibilityLiveData(::showOrHideCurrencyChoice)
        }
    }

    private fun handleEvent(event: CurrencyChoiceOnboardingEvent) {
        return when (event) {
            CurrencyChoiceOnboardingEvent.CloseApp -> {
                requireActivity().finishAndRemoveTask()
            }
        }
    }

    private fun showOrHideCurrencyChoice(show: Boolean) {
        val transition = MaterialContainerTransform(requireContext()).apply {
            startView = if (show) textInputLayout else currencyChoiceView
            endView = if (show) currencyChoiceView else textInputLayout

            scrimColor = Color.TRANSPARENT
            pathMotion = MaterialArcMotion()
            fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
        }
        if (show) {
            transition.doOnEnd {
                currencyChoiceView.searchEditText.showKeyboard()
            }
        }
        TransitionManager.beginDelayedTransition(constraintLayout, transition)
        if (show) {
            currencyChoiceView.makeVisible()
        } else {
            currencyChoiceView.makeGone()
        }
    }

}