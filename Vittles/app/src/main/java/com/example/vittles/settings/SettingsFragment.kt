package com.example.vittles.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vittles.services.notification.SharedPreference
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.example.vittles.R


/**
 *
 */
class SettingsFragment : DaggerFragment(), SettingsContract.View {

    @Inject
    lateinit var presenter: SettingsPresenter
    lateinit var lateView: View

    lateinit var sharedPreference: SharedPreference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        with(presenter) {
            start(this@SettingsFragment)
        }
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lateView = view
        sharedPreference = SharedPreference(lateView.context)

        initViews()
    }

    fun initViews() {
        setListeners()
        var notification  =  sharedPreference.getValueBoolen("Notification", false)
        switch1.isChecked = notification

//         var notificationTime = sharedPreference.getValueInt("NOTIFICATION_TIME")
//        notification_time[sharedPreference]
        println(sharedPreference.getValueInt("NOTIFICATION_TIME"))

    }

    fun setListeners() {

        switch1.setOnCheckedChangeListener { view, isChecked ->
            if (isChecked) {
                sharedPreference.save("Notification", true)
                println("@@@@@@@@@@@@@@@@@@@@ AAN")
            } else {
                sharedPreference.save("Notification", false)
                println("@@@@@@@@@@@@@@@@@@@@ UIT")
            }
        }



        notification_time.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
//                sharedPreference.save("NOTIFICATION_TIME", selectedItem)
                sharedPreference.save("NOTIFICATION_TIME", position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        })



    }
}