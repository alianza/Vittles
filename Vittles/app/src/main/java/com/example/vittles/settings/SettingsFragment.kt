package com.example.vittles.settings


import android.os.Bundle
import com.example.vittles.R
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat
import javax.inject.Inject


/**
 *
 */
class SettingsFragment : PreferenceFragmentCompat(){

    @Inject
    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.start(this)
    }

    override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting, rootKey)
    }



}
