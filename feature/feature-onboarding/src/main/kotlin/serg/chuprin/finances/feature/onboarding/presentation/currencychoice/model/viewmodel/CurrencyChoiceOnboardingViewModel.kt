package serg.chuprin.finances.feature.onboarding.presentation.currencychoice.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import serg.chuprin.finances.core.api.di.scopes.ScreenScope
import serg.chuprin.finances.core.api.presentation.model.viewmodel.BaseStoreViewModel
import serg.chuprin.finances.feature.onboarding.presentation.currencychoice.model.cells.CurrencyCell
import serg.chuprin.finances.feature.onboarding.presentation.currencychoice.model.store.CurrencyChoiceOnboardingEvent
import serg.chuprin.finances.feature.onboarding.presentation.currencychoice.model.store.CurrencyChoiceOnboardingIntent
import serg.chuprin.finances.feature.onboarding.presentation.currencychoice.model.store.CurrencyChoiceOnboardingStore
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 06.04.2020.
 */
@ScreenScope
class CurrencyChoiceOnboardingViewModel @Inject constructor(
    store: CurrencyChoiceOnboardingStore
) : BaseStoreViewModel<CurrencyChoiceOnboardingIntent>() {

    val eventsLiveData: LiveData<CurrencyChoiceOnboardingEvent> =
        store.observeEventsAsLiveData()

    val currencyCellsLiveData: LiveData<List<CurrencyCell>> =
        store.observeParticularStateAsLiveData { state -> state.currentCurrencyCells }

    val doneButtonEnabledLiveData: LiveData<Boolean> =
        store.observeParticularStateAsLiveData { state -> state.doneButtonIsEnabled }

    val chosenCurrencyDisplayNameLiveData: LiveData<String> =
        store.observeParticularStateAsLiveData { state -> state.chosenCurrencyDisplayName }

    val currencyPickerVisibilityLiveData =
        store.observeParticularStateAsLiveData { state -> state.currencyPickerIsVisible }

    init {
        store.start(intentsFlow(), viewModelScope)
    }

}