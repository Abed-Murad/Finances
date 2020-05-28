package serg.chuprin.finances.feature.transactions.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_transactions_report.*
import kotlinx.android.synthetic.main.fragment_transactions_report.view.*
import serg.chuprin.finances.core.api.presentation.extensions.arguments
import serg.chuprin.finances.core.api.presentation.extensions.setupToolbar
import serg.chuprin.finances.core.api.presentation.model.viewmodel.extensions.viewModelFromComponent
import serg.chuprin.finances.core.api.presentation.screen.arguments.TransactionsReportScreenArguments
import serg.chuprin.finances.core.api.presentation.view.BaseFragment
import serg.chuprin.finances.core.api.presentation.view.setEnterSharedElementTransition
import serg.chuprin.finances.core.api.presentation.view.setExitSharedElementTransition
import serg.chuprin.finances.feature.transactions.R
import serg.chuprin.finances.feature.transactions.di.TransactionsReportComponent

/**
 * Created by Sergey Chuprin on 11.05.2020.
 */
class TransactionsReportFragment : BaseFragment(R.layout.fragment_transactions_report) {

    private val screenArguments by arguments<TransactionsReportScreenArguments>()

    private val viewModel by viewModelFromComponent {
        TransactionsReportComponent.get(screenArguments)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)!!.apply {
            constraintLayout.transitionName = screenArguments.transitionName
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setExitSharedElementTransition()
        setEnterSharedElementTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(toolbar) {
            setDisplayHomeAsUpEnabled(true)
        }
    }

}