package serg.chuprin.finances.feature.dashboard.setup.impl.presentation.model.formatter

import serg.chuprin.finances.core.api.presentation.model.manager.ResourceManger
import serg.chuprin.finances.feature.dashboard.setup.impl.R
import serg.chuprin.finances.feature.dashboard.setup.presentation.domain.model.DashboardWidgetType
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 29.11.2020.
 */
class DashboardWidgetNameFormatter @Inject constructor(
    private val resourceManger: ResourceManger
) {

    fun format(widgetType: DashboardWidgetType): String {
        val stringRes = when (widgetType) {
            DashboardWidgetType.BALANCE -> R.string.dashboard_widget_balance
            DashboardWidgetType.CATEGORIES -> R.string.dashboard_widget_categories
            DashboardWidgetType.MONEY_ACCOUNTS -> R.string.dashboard_widget_money_accounts
            DashboardWidgetType.RECENT_TRANSACTIONS -> R.string.dashboard_widget_money_accounts
        }
        return resourceManger.getString(stringRes)
    }

}