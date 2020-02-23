package com.example.vittles.dashboard.productlist.ui.list

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_product.view.*
import android.graphics.*
import com.example.vittles.enums.DeleteType
import com.example.vittles.dashboard.model.ProductViewModel
import java.lang.UnsupportedOperationException
import kotlin.math.sign

class ProductItemTouchHelper(
    val callback: ProductItemTouchCallback
) : ItemTouchHelper(callback) {

    class ProductItemTouchCallback(
        private val context: Context,
        private val onSwiped: (ProductViewModel, DeleteType) -> Unit
    ) : Callback() {

        var products: List<ProductViewModel> = listOf()
        private val swipeAnimator = ProductItemSwipeAnimator()

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            /* not allowed to change order of items */
            return false
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return makeMovementFlags(0, LEFT or RIGHT)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val product = products[viewHolder.adapterPosition]
            val deleteType = when (direction) {
                RIGHT -> DeleteType.EATEN
                LEFT -> DeleteType.THROWN_AWAY
                else -> throw UnsupportedOperationException()
            }
            swipeAnimator.clear(viewHolder)
            onSwiped(product, deleteType)
        }

        override fun clearView(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ) {
            swipeAnimator.clear(viewHolder)
            super.clearView(recyclerView, viewHolder)
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            if (!isCurrentlyActive && dX == 0f) {
                swipeAnimator.clear(viewHolder)
                removeDecoratorOnLastProductItem(viewHolder.itemView)
            } else {
                /* negative -> swipe right, positive -> swipe left */
                when (sign(dX)) {
                    -1.0f -> swipeAnimator.swipe(context, c, viewHolder, recyclerView, RIGHT, dX)
                    1.0f, 0.0f -> swipeAnimator.swipe(context, c, viewHolder, recyclerView, LEFT, dX)
                }
            }
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        private fun removeDecoratorOnLastProductItem(view: View) {
            val product: ProductViewModel? =
                products.find { product -> product.uid == view.id }

            if (products.lastOrNull() == product) {
                view.borderDecorator.visibility = View.INVISIBLE
            } else {
                view.borderDecorator.visibility = View.VISIBLE
            }
        }
    }
}