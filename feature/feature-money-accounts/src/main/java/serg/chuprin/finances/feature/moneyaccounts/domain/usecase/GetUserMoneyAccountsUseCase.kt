package serg.chuprin.finances.feature.moneyaccounts.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import serg.chuprin.finances.core.api.domain.model.MoneyAccountBalances
import serg.chuprin.finances.core.api.domain.repository.UserRepository
import serg.chuprin.finances.core.api.domain.service.MoneyAccountService
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 06.05.2020.
 */
class GetUserMoneyAccountsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val moneyAccountService: MoneyAccountService
) {

    fun execute(): Flow<MoneyAccountBalances> {
        return userRepository
            .currentUserSingleFlow()
            .flatMapLatest { user ->
                moneyAccountService.moneyAccountBalancesFlow(user)
            }
    }

}