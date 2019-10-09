package com.example.vittles.popups

class PopupBase(override val header: String,
                override val subText: String,
                override val popupDuration: Long? = null) : IPopupBase

class PopupButton(override val text: String,
                  override val action: (() -> Unit)? = null) : IPopupButton
