package com.example.vittles.services.popups

interface OptionTextProvider<T> {

    fun getText(option: T): String
}