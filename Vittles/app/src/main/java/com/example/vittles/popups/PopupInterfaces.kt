package com.example.vittles.popups

internal interface IPopupBase{
    val header: String
    val subText: String
    val popupDuration: Long?
}

internal interface IPopupButton{
    val text: String
    val action: (() -> Unit)?


}