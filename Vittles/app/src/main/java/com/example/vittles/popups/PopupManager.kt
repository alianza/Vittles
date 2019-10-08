package com.example.vittles.popups

import android.os.Handler
import android.os.Bundle
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.timerTask


internal class PopupManager() {

    private var button: Unit? = null
    private var buttonLeft: Unit? = null
    private var buttonRight: Unit? = null
    private var i = 1

    //Singleton
    companion object{
        private var INSTANCE: PopupManager? = null

        val instance: PopupManager
            get(){
                if(INSTANCE == null){
                    INSTANCE = PopupManager()
                }

                return INSTANCE!!
            }
    }

    /**
     * Displays a popup with a header and subText.
     *
     * @param header The header of the popup.
     * @param subText The subtext in a popup.
     * @param popupTime The amount of milliseconds the popup should be active. If it's null the popup stays active.
     */
    internal fun showPopup(header: String, subText: String, popupTime: Int? = null){

        //TODO: Make sure the popup is shown with the right elements
        autoClosePopup(popupTime)
        throw NotImplementedError()
    }

    /**
     * Displays a popup with the header, subText and one button.
     *
     * @param header The header of the popup.
     * @param subText The subtext in a popup.
     * @param button A unit that should be called when the button on the popup is clicked. If null the button will close the popup.
     * @param popupTime The amount of milliseconds the popup should be active. If it's null the popup stays active.
     */
    internal fun showPopup(header: String, subText: String, button: (() -> Unit)?, popupTime: Int? = null){
        showPopup(header, subText, popupTime)
        throw NotImplementedError()
    }

    /**
     * Displays a popup with the header, subText and two buttons.
     *
     * @param header The header of the popup.
     * @param subText The subtext in a popup.
     * @param buttonLeft button A unit that should be called when the left button on the popup is clicked. If null the button will close the popup.
     * @param buttonRight button A unit that should be called when the right button on the popup is clicked. If null the button will close the popup.
     * @param popupTime The amount of milliseconds the popup should be active. If it's null the popup stays active.
     */
    internal fun showPopup(header: String, subText: String, buttonLeft: (() -> Unit)?, buttonRight: (() -> Unit)?, popupTime: Int? = null){
        showPopup(header, subText, popupTime)
        throw NotImplementedError()
    }

    /**
     * Closes the active popup
     *
     */
    internal fun closePopup(){
        showPopup("ha", "ja", null, { printLine() })
        throw NotImplementedError()
    }

    /**
     * Handles a button action
     *
     * @param action The action to handle
     */
    private fun buttonAction(action: Unit){
        if (action != null) {
            action
        } else{
            closePopup()
        }
    }

    /**
     * This method makes sure the popup is closed after a certain amount of milliseconds.
     *
     * @param duration The amount of milliseconds the popup is active. If it's null the popup won't close automatically
     */
    private fun autoClosePopup(duration: Int?) {
        if (duration == null){
            return
        }

        Timer().schedule(duration.toLong()) {
            closePopup()
        }
    }

    //TODO: Delete this placeholder method
    private fun printLine(){
        println("ja")
    }


}