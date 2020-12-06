package serg.chuprin.finances.feature.userprofile.presentation.model.builder

import serg.chuprin.finances.core.api.domain.model.User
import serg.chuprin.finances.core.api.presentation.formatter.DataPeriodFormatter
import serg.chuprin.finances.core.api.presentation.model.cells.BaseCell
import serg.chuprin.finances.feature.userprofile.presentation.model.cells.UserProfileDataPeriodTypeCell
import serg.chuprin.finances.feature.userprofile.presentation.model.cells.UserProfileImageCell
import serg.chuprin.finances.feature.userprofile.presentation.model.cells.UserProfileLogoutCell
import serg.chuprin.finances.feature.userprofile.presentation.model.cells.UserProfileSetupDashboardWidgetsCell
import javax.inject.Inject

/**
 * Created by Sergey Chuprin on 31.07.2020.
 */
class UserProfileCellsBuilder @Inject constructor(
    private val dataPeriodFormatter: DataPeriodFormatter
) {

    fun build(user: User): List<BaseCell> {
        val profileCell = UserProfileImageCell(
            email = user.email,
            imageUrl = user.photoUrl,
            username = user.displayName
        )
        val dataPeriodTypeCell = UserProfileDataPeriodTypeCell(
            dataPeriodType = user.dataPeriodType,
            periodTypeDisplayName = dataPeriodFormatter.formatType(user.dataPeriodType)
        )
        return mutableListOf(
            profileCell,
            dataPeriodTypeCell,
            UserProfileSetupDashboardWidgetsCell(),
            UserProfileLogoutCell()
        )
    }

}