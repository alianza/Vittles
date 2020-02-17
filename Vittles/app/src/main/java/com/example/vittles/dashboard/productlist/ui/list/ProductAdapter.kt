package com.example.vittles.dashboard.productlist.ui.list

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vittles.R
import com.example.vittles.dashboard.model.ProductViewModel
import kotlinx.android.synthetic.main.item_product.view.*
import javax.inject.Inject

class ProductAdapter @Inject constructor(
    private val onItemClicked: (ProductViewModel) -> Unit,
    private val onRemoveItemClicked: (ProductViewModel) -> Unit,
    private val itemTouchHelper: ProductItemTouchHelper
) :
    ListAdapter<ProductViewModel, ProductAdapter.ViewHolder>(
        itemDiff
    ), Filterable {

    lateinit var context: Context
    private val filter = ProductListFilter(this)

    var products: ArrayList<ProductViewModel> = arrayListOf()
        set(value) {
            field = value
            itemTouchHelper.products = value
        }

    var filteredProducts: ArrayList<ProductViewModel> = arrayListOf()
        set(value) {
            field = value
            itemTouchHelper.products = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).uid.toLong()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getFilter(): Filter = filter

    override fun submitList(list: List<ProductViewModel>?) {
        list?.let { products = ArrayList(list) }
        super.submitList(list)
    }

    fun submitFilteredList(list: List<ProductViewModel>) {
        filteredProducts = ArrayList(list)
        super.submitList(list)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            product: ProductViewModel
        ) {
            itemView.productId.text = product.uid.toString()
            itemView.id = product.uid

            // Assign daysLeft string to 99+ or 99- when greater or smaller than 99 or -99.
            val daysLeft: String = when {
                product.daysRemaining > context.getString(R.string.maxDaysRemaining).toInt() -> context.getString(
                    R.string.maxDaysRemaining
                ) + "+"
                product.daysRemaining < -context.getString(R.string.maxDaysRemaining).toInt() -> context.getString(
                    R.string.maxDaysRemaining
                ) + "-"
                else -> product.daysRemaining.toString()
            }

            // LayoutParams for setting margins programmatically
            val lp = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
            // Last product in list, remove decorator and add extra bottom-margin
            if (currentList.lastOrNull() == product) {
                itemView.borderDecorator.visibility = View.INVISIBLE
                val unbounded = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                itemView.measure(unbounded, unbounded)
                val bottomMargin = itemView.measuredHeight
                lp.setMargins(0, 0, 0, bottomMargin)
            } else {
                itemView.borderDecorator.visibility = View.VISIBLE
                lp.setMargins(0, 0, 0, 0)
            }
            itemView.layoutParams = lp

            // Set values in layout
            itemView.tvName.text = product.productName
            itemView.tvDate.text = context.resources.getString(
                R.string.expiration_format,
                product.expirationDate.dayOfMonth.toString(),
                product.expirationDate.monthOfYear.toString(),
                product.expirationDate.year.toString()
            )
            itemView.tvDaysLeft.text = daysLeft
            itemView.btnRemove.setOnClickListener { this@ProductAdapter.onRemoveItemClicked(product) }
            itemView.setOnClickListener { this@ProductAdapter.onItemClicked(product) }

            // Set the colors
            itemView.ivColor.setColorFilter(
                ContextCompat.getColor(
                    context,
                    product.getIndicationColor().value
                ), PorterDuff.Mode.MULTIPLY
            ) //Circle
            itemView.tvDaysLeft.setTextColor(
                ContextCompat.getColor(
                    context,
                    product.getIndicationColor().value
                )
            ) //DaysLeft number
        }
    }

    companion object {

        private val itemDiff = object : DiffUtil.ItemCallback<ProductViewModel>() {
            override fun areItemsTheSame(oldItem: ProductViewModel, newItem: ProductViewModel): Boolean {
                return oldItem.uid == newItem.uid
            }

            override fun areContentsTheSame(oldItem: ProductViewModel, newItem: ProductViewModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}