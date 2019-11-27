package com.example.vittles.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.example.vittles.R
import com.example.vittles.services.notification.NotificationScheduleService
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_settings.*


/**
 * Fragment class for the Settings. This is the fragment that shows the settings.
 *
 *@author Fethi Tewelde
 */
class SettingsFragment : DaggerFragment(), SettingsContract.View {


    /** To Store shared preferences(data) in the form of value-key*/
    lateinit var sharedPreference: SharedPreference

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
    override fun initViews() {

        setListeners()

        val notificationToggle = sharedPreference.getValueBoolean("Notification", false)
        notification_toggle.isChecked = notificationToggle

        val notificationTimeSelection = sharedPreference.getValueInt("NOTIFICATION_TIME")
        notification_timer.setSelection(notificationTimeSelection)

    }

    /**
     * Sets all necessary event listeners on ui elements
     *
     */
    override fun setListeners() {

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
                sharedPreference.save("NOTIFICATION_TIME", position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


    }
}