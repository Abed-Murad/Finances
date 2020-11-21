package serg.chuprin.finances.feature.onboarding.presentation.currencychoice.view

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.activity.addCallback
import androidx.core.transition.doOnEnd
import androidx.core.transition.doOnStart
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis
import kotlinx.android.synthetic.main.fragment_onboarding_currency_choice.*
import serg.chuprin.finances.core.api.presentation.currencychoice.model.store.CurrencyChoiceIntent
import serg.chuprin.finances.core.api.presentation.model.viewmodel.extensions.viewModelFromComponent
import serg.chuprin.finances.core.api.presentation.view.BaseFragment
import serg.chuprin.finances.core.api.presentation.view.extensions.makeGone
import serg.chuprin.finances.core.api.presentation.view.extensions.makeVisible
import serg.chuprin.finances.core.api.presentation.view.extensions.onClick
import serg.chuprin.finances.feature.onboarding.R
import serg.chuprin.finances.feature.onboarding.presentation.currencychoice.model.store.CurrencyChoiceOnboardingEvent
import serg.chuprin.finances.feature.onboarding.presentation.currencychoice.model.store.CurrencyChoiceOnboardingIntent
import serg.chuprin.finances.feature.onboarding.presentation.currencychoice.view.CurrencyChoiceOnboardingFragmentDirections.navigateFromCurrencyChoiceOnboardingToAccountsSetupOnboarding
import serg.chuprin.finances.feature.onboarding.presentation.launch.di.OnboardingFeatureComponent

/**
 * Created by Sergey Chuprin on 05.04.2020.
 */
class CurrencyChoiceOnboardingFragment :
    BaseFragment(R.layout.fragment_onboarding_currency_choice) {

    private val viewModel by viewModelFromComponent {
        OnboardingFeatureComponent.get().currencyChoiceComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            viewModel.dispatchIntent(CurrencyChoiceOnboardingIntent.BackPress)
        }

        val backward = MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, false)
        reenterTransition = backward

        val forward = MaterialSharedAxis.create(requireContext(), MaterialSharedAxis.X, true)
        exitTransition = forward
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animateFab(savedInstanceState, view)

        currencyChoiceView.onCurrencyCellChosen = { cell ->
            viewModel.dispatch(CurrencyChoiceIntent.ChooseCurrency(cell))
        }
        currencyChoiceView.onCloseClicked = {
            viewModel.dispatch(CurrencyChoiceIntent.CloseCurrencyPicker)
        }
        currencyChoiceView.onSearchQueryChanged = { searchQuery ->
            viewModel.dispatch(CurrencyChoiceIntent.SearchCurrencies(searchQuery))
        }
        chosenCurrencyTextView.onClick {
            viewModel.dispatch(CurrencyChoiceIntent.ClickOnCurrencyPicker)
        }
        doneButton.onClick {
            viewModel.dispatchIntent(CurrencyChoiceOnboardingIntent.ClickOnDoneButton)
        }

        with(viewModel) {
            eventsLiveData(::handleEvent)
            doneButtonEnabledLiveData(doneButton::setEnabled)
            cellsLiveData(currencyChoiceView::setCells)
            currencyPickerVisibilityLiveData(::showOrHideCurrencyChoice)
            chosenCurrencyDisplayNameLiveData(chosenCurrencyTextView::setText)
        }
    }

    private fun handleEvent(event: CurrencyChoiceOnboardingEvent) {
        return when (event) {
            CurrencyChoiceOnboardingEvent.CloseApp -> {
                requireActivity().finishAndRemoveTask()
            }
            CurrencyChoiceOnboardingEvent.NavigateToAccountsSetup -> {
                navigateFromCurrencyChoiceOnboardingToAccountsSetupOnboarding().run {
                    navController.navigate(this)
                }
            }
        }
    }

    private fun showOrHideCurrencyChoice(show: Boolean) {
        val transition = MaterialContainerTransform(requireContext()).apply {
            startView = if (show) chosenCurrencyTextView else currencyChoiceView
            endView = if (show) currencyChoiceView else chosenCurrencyTextView

            scrimColor = Color.TRANSPARENT
            pathMotion = MaterialArcMotion()
            fadeMode = MaterialContainerTransform.FADE_MODE_THROUGH
        }
        if (show) {
            transition.doOnStart {
                currencyChoiceView.resetScroll()
                currencyChoiceView.clearSearchQuery()
            }
            transition.doOnEnd {
                currencyChoiceView.showKeyboard()
            }
        }
        TransitionManager.beginDelayedTransition(constraintLayout, transition)
        if (show) {
            currencyChoiceView.makeVisible()
        } else {
            currencyChoiceView.makeGone()
        }
    }

    private fun animateFab(savedInstanceState: Bundle?, view: View) {

        fun showFabTextAndExtend() {
            doneButton.setText(R.string.onboarding_currency_choice_button)
            doneButton.extend()
        }

        // Animate on first screen show.
        if (savedInstanceState == null) {
            view.postDelayed(
                {
                    doneButton?.show(object : ExtendedFloatingActionButton.OnChangedCallback() {
                        override fun onShown(extendedFab: ExtendedFloatingActionButton?) {
                            showFabTextAndExtend()
                        }
                    })
                },
                400
            )
        } else {
            doneButton.makeVisible()
            showFabTextAndExtend()
        }
    }

}