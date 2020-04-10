package serg.chuprin.finances.feature.onboarding.presentation.currencychoice.view.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_currency_choice.view.*
import serg.chuprin.adapter.DiffMultiViewAdapter
import serg.chuprin.finances.core.api.extensions.EMPTY_STRING
import serg.chuprin.finances.core.api.presentation.view.adapter.diff.DiffCallback
import serg.chuprin.finances.core.api.presentation.view.extensions.*
import serg.chuprin.finances.feature.onboarding.R
import serg.chuprin.finances.feature.onboarding.presentation.currencychoice.model.cells.CurrencyCell
import serg.chuprin.finances.feature.onboarding.presentation.currencychoice.view.adapter.renderer.CurrencyCellRenderer

/**
 * Created by Sergey Chuprin on 05.04.2020.
 */
class CurrencyChoiceListView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    lateinit var onCloseClicked: () -> Unit
    lateinit var onSearchQueryChanged: (String) -> Unit
    lateinit var onCurrencyCellChosen: (cell: CurrencyCell) -> Unit

    private val view = View.inflate(context, R.layout.view_currency_choice, this)
    private val cellsAdapter = DiffMultiViewAdapter(DiffCallback<CurrencyCell>()).apply {
        registerRenderer(CurrencyCellRenderer())
        clickListener = { cell, _, _ ->
            hideKeyboard()
            onCurrencyCellChosen.invoke(cell)
        }
    }

    init {
        with(recyclerView) {
            setHasFixedSize(true)
            adapter = cellsAdapter
            layoutManager = LinearLayoutManager(context)
            // Fading edge is not working from XML.
            isVerticalFadingEdgeEnabled = true
            setFadingEdgeLength(context.dpToPx(16))

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        hideKeyboard()
                    }
                }
            })
        }
        background = ColorDrawable(context.getBackgroundColor())
        searchEditText.doAfterTextChanged { editable ->
            if (!searchEditText.shouldIgnoreChanges) {
                editable?.toString()?.let(onSearchQueryChanged)
            }
        }
        closeImageView.onClick {
            hideKeyboard()
            onCloseClicked.invoke()
        }
    }

    fun setCells(cells: List<CurrencyCell>) = cellsAdapter.setItems(cells)

    fun resetScroll() = recyclerView.layoutManager!!.scrollToPosition(0)

    fun clearSearchQuery() {
        if (!searchEditText.text.isNullOrEmpty()) {
            searchEditText.doIgnoringChanges {
                setText(EMPTY_STRING)
            }
        }
    }

    fun showKeyboard() = searchEditText.showKeyboard()

    private fun hideKeyboard() {
        with(searchEditText) {
            if (isFocused) {
                hideKeyboard()
            }
        }
    }

}