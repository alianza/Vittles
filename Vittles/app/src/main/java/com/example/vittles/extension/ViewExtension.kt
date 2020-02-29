package com.example.vittles.extension

import android.view.View

/**
 * @author Jeroen Flietstra
 *
 */

fun View.setVisible() {
    visibility = View.VISIBLE
}

fun View.setInvisible() {
    visibility = View.INVISIBLE
}

fun View.setGone() {
    visibility = View.GONE
}