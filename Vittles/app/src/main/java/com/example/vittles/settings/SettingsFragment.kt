package com.example.vittles.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.lifecycle.Observer
import com.example.data.settings.SharedPreferenceHelper
import com.example.domain.settings.model.NotificationSchedule
import com.example.vittles.R
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject


/**
 * Fragment class for the Settings. This is the fragment that shows the settings.
 *
 *@author Fethi Tewelde
 */
class SettingsFragment : DaggerFragment() {

    @Inject
    lateinit var presenter: SettingsPresenter

    /** {@inheritDoc} */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        presenter.start(this)
        presenter.startPresenting(requireContext())
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    /** {@inheritDoc} */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }

    /**
     * Sets EventListeners
     * sets default values to the notification toggle
     * shows/keeps the last selected value of notification time
     *
     */
    private fun initViews() {

        setListeners()

        presenter.notificationEnabled.observe(this, Observer {
            notification_toggle.isChecked = it
        })

        presenter.notificationSchedule.observe(this, Observer {
            notification_timer.setSelection(it.ordinal)
        })

        presenter.vibrationEnabled.observe(this, Observer {
            vibration_toggle.isChecked = it
        })
    }

    /**
     * Sets all necessary event listeners on ui elements
     *
     */
    private fun setListeners() {

        /*
         * called when the vibration switch is switched
         *
         */
        vibration_toggle.setOnCheckedChangeListener { _, isChecked ->
            presenter.onVibrationEnabledChanged(isChecked)
        }

        /*
         * called when the notification switch is switched
         *
         */
        notification_toggle.setOnCheckedChangeListener { _, isChecked ->
            presenter.onNotificationEnabledChanged(isChecked)
        }


        /*
         * called when time is selected from the spinner
         *
         */
        notification_timer.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val notificationSchedule = NotificationSchedule.values()[position]

                presenter.onNotificationScheduleChanged(notificationSchedule)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


    }
}