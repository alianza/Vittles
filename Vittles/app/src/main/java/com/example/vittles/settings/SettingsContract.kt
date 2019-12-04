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
        fun onAdvancedClick()
        fun expandAdvancedSettings()
        fun fadeOutAnim(elem: android.view.View)
        fun fadeInAnim(elem: android.view.View)
        fun onRemoveSavedProductsClick()
        fun onProductDictionaryClearSuccess()
        fun onProductDictionaryClearFail()
        fun removeSavedProducts()
    }

    interface Presenter{
        fun clearProductDictionary()
    }
}