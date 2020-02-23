package com.example.vittles.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Forces the keyboard to display
 *
 */
fun showKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInputFromWindow(view.windowToken, InputMethodManager.SHOW_FORCED, 0)
}

/**
 * Dismisses the keyboard.
 *
 */
fun dismissKeyboard(context: Context, view: View) {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}