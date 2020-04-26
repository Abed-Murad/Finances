package serg.chuprin.finances.core.api.di.provider

import serg.chuprin.finances.core.api.presentation.model.builder.DataPeriodTypePopupMenuCellsBuilder

/**
 * Created by Sergey Chuprin on 23.04.2020.
 */
interface CoreBuildersProvider {

    val dataPeriodTypePopupMenuCellsBuilder: DataPeriodTypePopupMenuCellsBuilder

}