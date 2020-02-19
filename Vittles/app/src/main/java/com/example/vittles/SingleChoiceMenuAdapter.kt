package com.example.vittles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class SingleChoiceMenuAdapter(
    private val mContext: Context,
    private val resource: Int,
    private val items: Array<String>,
    private val default: String
) : ArrayAdapter<String>(mContext, resource, items) {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): String? {
        return items[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView
            ?: LayoutInflater.from(parent.context)
                .inflate(resource, parent, false)
        view.findViewById<TextView>(R.id.tvSingleOption).text = items[position]
        if (default == items[position]) {
            view.findViewById<ImageView>(R.id.ivCheckbox).setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ic_circle_darkened)
            )
        } else {
            view.findViewById<ImageView>(R.id.ivCheckbox).setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ic_circle)
            )
        }
        return view
    }
}