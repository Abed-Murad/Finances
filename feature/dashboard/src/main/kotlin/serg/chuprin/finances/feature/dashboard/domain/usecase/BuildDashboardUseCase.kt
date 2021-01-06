package serg.chuprin.finances.feature.dashboard.domain.usecase

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import serg.chuprin.finances.core.api.domain.model.User
import serg.chuprin.finances.core.api.domain.model.moneyaccount.query.MoneyAccountsQuery
import serg.chuprin.finances.core.api.domain.model.period.DataPeriod
import serg.chuprin.finances.core.api.domain.repository.MoneyAccountRepository
import serg.chuprin.finances.core.api.domain.repository.UserRepository
import serg.chuprin.finances.feature.dashboard.domain.builder.DashboardWidgetBuilder
import serg.chuprin.finances.feature.dashboard.domain.model.Dashboard
import serg.chuprin.finances.feature.dashboard.domain.model.DashboardWidgets
import serg.chuprin.finances.feature.dashboard.domain.repository.DashboardDataPeriodRepository
import serg.chuprin.finances.feature.dashboard.setup.domain.model.CustomizableDashboardWidget
import serg.chuprin.finances.feature.dashboard.setup.domain.model.DashboardWidgetType
import serg.chuprin.finances.feature.dashboard.setup.domain.repository.DashboardWidgetsRepository
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 17.04.2020.
 */
class BuildDashboardUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val moneyAccountRepository: MoneyAccountRepository,
    private val dataPeriodRepository: DashboardDataPeriodRepository,
    private val dashboardWidgetsRepository: DashboardWidgetsRepository,
    private val widgetBuilders: Set<@JvmSuppressWildcards DashboardWidgetBuilder<*>>
) {

    @OptIn(FlowPreview::class)
    fun execute(): Flow<Dashboard> {
        return userRepository
            .currentUserSingleFlow()
            .flatMapLatest { user ->
                moneyAccountRepository.accountsFlow(MoneyAccountsQuery(ownerId = user.id))
            }
            // Don't bother downstream with money account updates
            // but only if new account created or all accounts deleted.
            .distinctUntilChanged { old, new ->
                old.size == new.size || !(old.isEmpty() || new.isEmpty())
            }
            .flatMapConcat { moneyAccounts ->
                if (moneyAccounts.isEmpty()) {
                    flowOf(Dashboard(hasNoMoneyAccounts = true))
                } else {
                    combine(
                        userRepository
                            .currentUserSingleFlow(),
                        dataPeriodRepository
                            .currentDataPeriodFlow
                            .distinctUntilChanged(),
                        getOrderedWidgetsFlow(),
                        transform = { currentUser, currentPeriod, widgets ->
                            Triple(currentUser, currentPeriod, widgets)
                        }
                    ).flatMapLatest { (currentUser, currentPeriod, orderedWidgets) ->
                        buildDashboard(currentUser, currentPeriod, orderedWidgets)
                    }
                }
            }
    }

    @OptIn(FlowPreview::class)
    private fun buildDashboard(
        currentUser: User,
        currentPeriod: DataPeriod,
        orderedWidgets: Map<DashboardWidgetType, Int>
    ): Flow<Dashboard> {
        val flows = orderedWidgets.mapNotNull { (widgetType) ->
            widgetBuilders
                .firstOrNull { builder -> builder.isForType(widgetType) }
                ?.build(currentUser, currentPeriod)
        }

        return combine(flows) { arr ->
            arr.fold(
                initial = Dashboard(
                    user = currentUser,
                    hasNoMoneyAccounts = false,
                    currentDataPeriod = currentPeriod,
                    widgets = DashboardWidgets(orderedWidgets)
                ),
                { dashboard, widget ->
                    dashboard.copy(widgets = dashboard.widgets.put(widget))
                }
            )
        }.debounce(100)
    }

    private fun getOrderedWidgetsFlow(): Flow<Map<DashboardWidgetType, Int>> {
        return dashboardWidgetsRepository
            .widgetsFlow
            .map { widgets ->
                widgets
                    .filter(CustomizableDashboardWidget::isEnabled)
                    .sortedBy(CustomizableDashboardWidget::order)
                    .mapIndexed { index, customizableDashboardWidget ->
                        customizableDashboardWidget.widgetType to index
                    }
                    .toMap()
            }
    }

}