package serg.chuprin.finances.core.api.di.provider

import serg.chuprin.finances.core.api.domain.usecase.MarkMoneyAccountAsFavoriteUseCase
import serg.chuprin.finances.core.api.domain.usecase.SearchCurrenciesUseCase

/**
 * Created by Sergey Chuprin on 10.04.2020.
 */
interface CoreUseCasesProvider {

    val searchCurrenciesUseCase: SearchCurrenciesUseCase

    val markMoneyAccountAsFavoriteUseCase: MarkMoneyAccountAsFavoriteUseCase

}