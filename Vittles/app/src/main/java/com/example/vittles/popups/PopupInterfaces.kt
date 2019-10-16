package com.example.vittles.popups

/**
 * The interface for the base of each popup.
 *
 * @author Arjen Simons
 */
internal interface IPopupBase{
    val header: String
    val subText: String
    val popupDuration: Long?
}

/**
 * The interface for the button on a popup.
 *
 * @author Arjen Simons
 */
internal interface IPopupButton{
    val text: String
    val action: (() -> Unit)?


}