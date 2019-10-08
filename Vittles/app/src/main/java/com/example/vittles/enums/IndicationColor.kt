package com.example.vittles.enums

import com.example.vittles.R

/**
 * Enumerator for indication colors on the main products activity.
 *
 * @author Jeroen Flietstra
 * @author Arjen Simons
 *
 * @property value Value of the color which is a color from the resources.
 */
enum class IndicationColor(val value: Int) {
    RED(R.color.red),
    YELLOW(R.color.yellow),
    GREEN(R.color.green)
}