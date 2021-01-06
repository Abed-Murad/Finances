package serg.chuprin.finances.feature.categories.impl.presentation.view.adapter.renderer

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import kotlinx.android.synthetic.main.view_category.*
import serg.chuprin.adapter.Click
import serg.chuprin.adapter.ContainerHolder
import serg.chuprin.adapter.ContainerRenderer
import serg.chuprin.adapter.LongClick
import serg.chuprin.finances.core.api.extensions.containsType
import serg.chuprin.finances.core.api.presentation.view.extensions.makeVisibleOrGone
import serg.chuprin.finances.core.api.presentation.view.extensions.onViewClick
import serg.chuprin.finances.feature.categories.impl.R
import serg.chuprin.finances.feature.categories.impl.presentation.model.cell.ParentCategoryCell

/**
 * Created by Sergey Chuprin on 28.12.2020.
 */
class ParentCategoryCellRenderer : ContainerRenderer<ParentCategoryCell>() {

    private companion object {
        private const val EXPANSION_ARROW_ANIMATION_DURATION = 400L
        private val animationInterpolator = FastOutSlowInInterpolator()
    }


    override val type: Int = R.layout.cell_parent_category

    override fun bindView(holder: ContainerHolder, model: ParentCategoryCell) {
        with(holder) {
            nameTextView.text = model.category.name
            transactionColorDot.imageTintList = ColorStateList.valueOf(model.color)

            expansionArrowImageView.makeVisibleOrGone(model.isExpansionAvailable)
            if (model.isExpansionAvailable) {
                expansionArrowImageView.rotation = if (model.isExpanded) -180f else 0f
            }
        }
    }

    override fun bindView(
        holder: ContainerHolder,
        model: ParentCategoryCell,
        payloads: MutableList<Any>
    ) {
        if (payloads.containsType<ParentCategoryCell.ExpansionChangedPayload>()) {
            animateExpansionArrow(holder.expansionArrowImageView, model.isExpanded)
        }
    }

    override fun onVhCreated(
        holder: ContainerHolder,
        clickListener: Click?,
        longClickListener: LongClick?
    ) {
        with(holder) {
            expansionArrowImageView.onViewClick { view ->
                clickListener?.onClick(view, adapterPosition)
            }
            itemView.onViewClick { view ->
                clickListener?.onClick(view, adapterPosition)
            }
        }
    }

    private fun animateExpansionArrow(expansionArrowImageView: ImageView, isExpanded: Boolean) {
        with(expansionArrowImageView) {
            animation?.cancel()
            animate()
                .setInterpolator(animationInterpolator)
                .rotation(if (isExpanded) -180f else 0f)
                .setDuration(EXPANSION_ARROW_ANIMATION_DURATION)
                .start()
        }
    }

}