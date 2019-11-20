package com.example.vittles.settings

import com.example.vittles.mvp.BasePresenter
import javax.inject.Inject

class SettingsPresenter @Inject internal constructor() :
    BasePresenter<SettingsFragment>(), SettingsContract.Presenter {
}