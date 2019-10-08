package com.example.vittles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vittles.model.SortOption
import kotlinx.android.synthetic.main.sort_option.view.*

class SortAdapter (private val sortingOptions: List<SortOption>) :
    RecyclerView.Adapter<SortAdapter.ViewHolder>(){

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
            LayoutInflater.from(context).inflate(R.layout.sort_option, parent, false)
        )
    }

    /**
     * Gets the amount of items in the ListView.
     *
     * @return The amount of items.
     */
    override fun getItemCount(): Int {
        return sortingOptions.size
    }

    /**
     * Updates the contents of the itemView to reflect the item at the given position.
     *
     * @param The ViewHolder that should be updated.
     * @param position The position of the item to be updated.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sortingOptions[position])
    }

    /**
     * Represents an element in the RecyclerView.
     *
     * @param itemView The xml file that represents an item.
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(sortOption: SortOption) {

            itemView.option.text = sortOption.name
        }
    }
}