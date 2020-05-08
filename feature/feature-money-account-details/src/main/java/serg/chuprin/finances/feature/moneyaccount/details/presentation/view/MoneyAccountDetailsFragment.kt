package serg.chuprin.finances.feature.moneyaccount.details.presentation.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.android.synthetic.main.fragment_money_account_details.*
import serg.chuprin.finances.core.api.presentation.extensions.arguments
import serg.chuprin.finances.core.api.presentation.model.cells.BaseCell
import serg.chuprin.finances.core.api.presentation.model.viewmodel.extensions.viewModelFromComponent
import serg.chuprin.finances.core.api.presentation.view.BaseFragment
import serg.chuprin.finances.core.api.presentation.view.SHARED_ELEMENT_TRANSITION_DURATION
import serg.chuprin.finances.core.api.presentation.view.adapter.DiffMultiViewAdapter
import serg.chuprin.finances.core.api.presentation.view.adapter.diff.DiffCallback
import serg.chuprin.finances.core.api.presentation.view.adapter.renderer.DateDividerCellRenderer
import serg.chuprin.finances.core.api.presentation.view.adapter.renderer.TransactionCellRenderer
import serg.chuprin.finances.core.api.presentation.view.adapter.renderer.ZeroDataCellRenderer
import serg.chuprin.finances.core.api.presentation.view.extensions.dpToPx
import serg.chuprin.finances.feature.moneyaccount.details.R
import serg.chuprin.finances.feature.moneyaccount.details.presentation.arguments.MoneyAccountDetailsScreenArguments
import serg.chuprin.finances.feature.moneyaccount.details.presentation.di.MoneyAccountDetailsComponent
import serg.chuprin.finances.feature.moneyaccount.details.presentation.model.store.MoneyAccountDetailsEvent

/**
 * Created by Sergey Chuprin on 07.05.2020.
 */
class MoneyAccountDetailsFragment : BaseFragment(R.layout.fragment_money_account_details) {

    private val screenArguments by arguments<MoneyAccountDetailsScreenArguments>()

    private val viewModel by viewModelFromComponent {
        MoneyAccountDetailsComponent.get(screenArguments)
    }

    private val cellsAdapter = DiffMultiViewAdapter(DiffCallback<BaseCell>()).apply {
        registerRenderer(ZeroDataCellRenderer())
        registerRenderer(TransactionCellRenderer())
        registerRenderer(DateDividerCellRenderer())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform(requireContext()).apply {
            containerColor = Color.WHITE
            duration = SHARED_ELEMENT_TRANSITION_DURATION
            fadeMode = MaterialContainerTransform.FADE_MODE_CROSS
            startShapeAppearanceModel = ShapeAppearanceModel().withCornerSize(
                requireContext().dpToPx(16).toFloat()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)!!.apply {
            transitionName =
                "${getString(R.string.transition_money_account)}${screenArguments.moneyAccountId.value}"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(transactionsRecyclerView) {
            adapter = cellsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        with(viewModel) {
            eventsLiveData(::handleEvent)
            cellsLiveData(cellsAdapter::setItems)
            balanceLiveData(balanceTextView::setText)
            accountNameLiveData(collapsingToolbarLayout::setText)
        }
    }

    private fun handleEvent(event: MoneyAccountDetailsEvent) {
        return when (event) {
            MoneyAccountDetailsEvent.CloseScreen -> {
                navController.navigateUp()
                Unit
            }
        }
    }

}