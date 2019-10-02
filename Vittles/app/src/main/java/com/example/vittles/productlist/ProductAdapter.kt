package com.example.vittles.productlist

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Product
import com.example.vittles.R
import com.example.vittles.productadd.AddProductActivity
import kotlinx.android.synthetic.main.item_product.view.*
import javax.inject.Inject

/**
 * Binds app-specific data to views that are displayed in the RecyclerView.
 *
 * @author Arjen Simons
 *
 * @property products The list of products that should be displayed in the RecyclerView.
 * @suppress DEPRECATION Suppress deprecation on 'Date' since the project is running on API 21.
 */
@Suppress("DEPRECATION")
class ProductAdapter @Inject constructor(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    lateinit var context: Context

    /**
     * Creates a new ViewHolder.
     *
     * @param parent The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return Returns the new ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        )
    }

    /**
     * Gets the amount of items in the ListView.
     *
     * @return The amount of items.
     */
    override fun getItemCount(): Int {
        return products.size
    }

    /**
     * Updates the contents of the itemView to reflect the item at the given position.
     *
     * @param The ViewHolder that should be updated.
     * @param position The position of the item to be updated.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(products[position])
    }

    /**
     * Represents an element in the RecyclerView.
     *
     * @param itemView The xml file that represents an item.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        /**
         * Sets the values of the itemView to the product values.
         *
         * @param product The product that is bound to the itemView.
         */
        fun bind(product: Product) {
            val daysLeft = product.getDaysRemaining()

            itemView.tvName.text = product.productName
            itemView.tvDate.text = context.resources.getString(
                R.string.expiration_format,
                product.expirationDate?.date.toString(),
                product.expirationDate?.month?.plus(AddProductActivity.MONTHS_OFFSET).toString(),
                product.expirationDate?.year?.plus(AddProductActivity.YEARS_OFFSET).toString())
            itemView.tvDaysLeft.text = daysLeft.toString()

            //Set the colors
//            itemView.ivColor.setColorFilter(ContextCompat.getColor(context, product.getIndicationColor()), PorterDuff.Mode.MULTIPLY) //Circle
//            itemView.tvDaysLeft.setTextColor(ContextCompat.getColor(context, product.getIndicationColor())) //DaysLeft number
        }
    }
}