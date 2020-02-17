package com.example.vittles.dashboard.productlist.ui.list

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.vittles.R
import kotlinx.android.synthetic.main.item_product.view.*
import java.lang.UnsupportedOperationException

class ProductItemSwipeAnimator {

    lateinit var rv: RecyclerView

    fun swipe(context: Context, c: Canvas, vh: RecyclerView.ViewHolder, recyclerView: RecyclerView, direction: Int, dX: Float) {
        if (!::rv.isInitialized) {
            rv = recyclerView
        }
        addSwipeStyle(vh)
        setBackground(context, c, vh, direction)
        transformIcon(context, c, vh, dX, direction)
    }

    fun clear(vh: RecyclerView.ViewHolder) {
        if (::rv.isInitialized) {
            clearSwipeStyle(vh)
        }
    }

    private fun addSwipeStyle(vh: RecyclerView.ViewHolder) {
        if (vh.layoutPosition > 0) {
            val viewHolderAbove =
                rv.findViewHolderForLayoutPosition(vh.layoutPosition - 1)!!
            viewHolderAbove.itemView.borderDecorator.visibility = View.INVISIBLE
        }
        vh.itemView.borderDecorator.visibility = View.INVISIBLE
        vh.itemView.ivSwipeBorders.visibility = View.VISIBLE
    }

    private fun clearSwipeStyle(vh: RecyclerView.ViewHolder) {
        if (vh.layoutPosition > 0) {
            val viewHolderAbove =
                rv.findViewHolderForLayoutPosition(vh.layoutPosition - 1)!!
            viewHolderAbove.itemView.borderDecorator.visibility = View.VISIBLE
        }
        vh.itemView.borderDecorator.visibility = View.VISIBLE
        vh.itemView.ivSwipeBorders.visibility = View.INVISIBLE
    }

    private fun setBackground(context: Context, c: Canvas, vh: RecyclerView.ViewHolder, direction: Int) {
        val background = ColorDrawable()
        background.color = when (direction) {
            ItemTouchHelper.RIGHT -> ContextCompat.getColor(context,
                SWIPE_RIGHT_CANVAS_COLOR
            )
            ItemTouchHelper.LEFT -> ContextCompat.getColor(context,
                SWIPE_LEFT_CANVAS_COLOR
            )
            else -> throw UnsupportedOperationException()
        }
        background.setBounds(
            vh.itemView.left, vh.itemView.top,
            vh.itemView.right, vh.itemView.bottom
        )
        background.draw(c)
    }

    private fun transformIcon(context: Context, canvas: Canvas, vh: RecyclerView.ViewHolder, dX: Float, direction: Int) {
        var icon = 0
        when (direction) {
            ItemTouchHelper.RIGHT -> {
                icon = when {
                    dX > -vh.itemView.width / 3 -> R.drawable.ic_delete_1
                    dX < -vh.itemView.width / 3 && dX > -vh.itemView.width / 2 -> R.drawable.ic_delete_2
                    dX < -vh.itemView.width / 2 -> R.drawable.ic_delete_3
                    else -> R.drawable.ic_delete_1
                }
            }
            ItemTouchHelper.LEFT -> {
                icon = when {
                    dX < vh.itemView.width / 3 -> R.drawable.ic_eaten_1
                    dX > vh.itemView.width / 3 && dX < vh.itemView.width / 2 -> R.drawable.ic_eaten_2
                    dX > vh.itemView.width / 2 -> R.drawable.ic_eaten_3
                    else -> R.drawable.ic_eaten_1
                }
            }
        }
        context.getDrawable(icon)?.let {
            drawIcon(canvas, vh, it, direction)
        }
    }

    private fun drawIcon(
        c: Canvas,
        viewHolder: RecyclerView.ViewHolder,
        icon: Drawable,
        direction: Int
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val iconTop = itemView.top + (itemHeight - icon.intrinsicHeight) / 2
        val iconMargin = (itemHeight - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight
        var iconLeft = 0
        var iconRight = 0

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                iconRight = itemView.right - iconMargin
            }
            ItemTouchHelper.LEFT -> {
                iconLeft = itemView.left + iconMargin
                iconRight = itemView.left + iconMargin + icon.intrinsicWidth
            }
        }

        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
        icon.draw(c)
    }

    companion object {
        private const val SWIPE_RIGHT_CANVAS_COLOR = R.color.red
        private const val SWIPE_LEFT_CANVAS_COLOR = R.color.green
    }
}