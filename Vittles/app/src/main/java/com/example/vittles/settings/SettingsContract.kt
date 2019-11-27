package com.example.vittles.settings


/**
 * MVP Contract for settings overview.
 *
 * @author Fethi Tewelde
 */
interface SettingsContract {
    interface View{
        fun initViews()
        fun setListeners()
    }

    interface Presenter{}
}