package serg.chuprin.finances.feature.dashboard.presentation.view.adapter.categories

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import kotlinx.android.synthetic.main.cell_widget_dashboard_categories.*
import serg.chuprin.adapter.*
import serg.chuprin.finances.feature.dashboard.R
import serg.chuprin.finances.feature.dashboard.presentation.model.cells.DashboardWidgetCell
import serg.chuprin.finances.feature.dashboard.presentation.view.adapter.categories.diff.DashboardCategoriesWidgetDiffCallback
import serg.chuprin.finances.feature.dashboard.presentation.view.adapter.categories.diff.payload.DashboardCategoriesWidgetChangedPayload
import serg.chuprin.finances.feature.dashboard.presentation.view.adapter.categories.page.DashboardExpenseCategoriesPageCellRenderer
import serg.chuprin.finances.feature.dashboard.presentation.view.adapter.categories.page.DashboardIncomeCategoriesPageCellRenderer

/**
 * Created by Sergey Chuprin on 24.04.2020.
 */
class DashboardCategoriesWidgetCellRenderer : ContainerRenderer<DashboardWidgetCell.Categories>() {

    override val type: Int = R.layout.cell_widget_dashboard_categories

    private val pageCellAdapter =
        DiffMultiViewAdapter(DashboardCategoriesWidgetDiffCallback()).apply {
            registerRenderer(DashboardIncomeCategoriesPageCellRenderer())
            registerRenderer(DashboardExpenseCategoriesPageCellRenderer())
        }

    override fun bindView(holder: ContainerHolder, model: DashboardWidgetCell.Categories) {
        pageCellAdapter.setItems(model.pageCells)
    }

    override fun bindView(
        holder: ContainerHolder,
        model: DashboardWidgetCell.Categories,
        payloads: MutableList<Any>
    ) {
        if (DashboardCategoriesWidgetChangedPayload in payloads) {
            pageCellAdapter.setItems(model.pageCells)
        }
    }

    override fun onVhCreated(
        holder: ContainerHolder,
        clickListener: Click?,
        longClickListener: LongClick?
    ) {
        with(holder.recyclerView) {
            adapter = pageCellAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

            LinearSnapHelper().attachToRecyclerView(this)
        }
        holder.pageIndicator.attachToRecyclerView(holder.recyclerView)
    }

}