package com.example.vittles.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.example.domain.settings.model.NotificationSchedule
import com.example.vittles.R
import com.example.vittles.services.notification.NotificationScheduleService
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject


/**
 * Fragment class for the Settings. This is the fragment that shows the settings.
 *
 *@author Fethi Tewelde
 */
class SettingsFragment : DaggerFragment() {


    /** To Store shared preferences(data) in the form of value-key*/
    lateinit var sharedPreference: SharedPreference

    @Inject
    lateinit var presenter: SettingsContract.Presenter

    /** @suppress */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    /** @suppress */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = SharedPreference(context!!)

        initViews()
    }

    /**
     * Sets EventListeners
     * sets default values to the notification toggle
     * shows/keeps the last selected value of notification time
     *
     */
    private fun initViews() {

        setListeners()

        val notificationToggle = sharedPreference.getValueBoolean("Notification", true)
        notification_toggle.isChecked = notificationToggle

        val notificationTimeSelection = sharedPreference.getValueInt("NOTIFICATION_TIME")
        notification_timer.setSelection(notificationTimeSelection)

        val vibrationToggle = sharedPreference.getValueBoolean("Vibration", true)
        vibration_toggle.isChecked = vibrationToggle
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
            if (isChecked) {
                sharedPreference.save("Vibration", true)
            }else{
                sharedPreference.save("Vibration", false)
            }
        }

        /*
         * called when the notification switch is switched
         *
         */
        notification_toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                sharedPreference.save("Notification", true)
                NotificationScheduleService.scheduleNotificationAudit(context!!)
            } else {
                sharedPreference.save("Notification", false)
                NotificationScheduleService.exitNotificationSchedule(context!!)
            }
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

//                sharedPreference.save("NOTIFICATION_TIME", position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


    }
}