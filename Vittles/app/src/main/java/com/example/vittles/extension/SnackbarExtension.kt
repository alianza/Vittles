package com.example.vittles.extension

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.example.vittles.R
import com.google.android.material.snackbar.Snackbar

fun createSnackbar(context: Context, parent: View, text: String, gravity: Int, length: Int): Snackbar {
    return Snackbar.make(parent, text, length).apply {
        setTextColor(Color.BLACK)
        view.background = ContextCompat.getDrawable(context, R.drawable.bg_cr)
        val lp = view.layoutParams as CoordinatorLayout.LayoutParams
        lp.gravity = gravity
        lp.width = (parent.width * 0.9).toInt()
        view.layoutParams = lp
    }
}