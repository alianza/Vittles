package com.example.data.settings

import android.content.Context
import com.example.domain.settings.SettingsRepository
import com.example.domain.settings.model.NotificationSchedule
import javax.inject.Inject

class SharedPrefsSettingsRepository @Inject constructor(private val context: Context): SettingsRepository {

    override fun getNotificationSchedule(): NotificationSchedule {
        val a = 0

        NotificationSchedule.values()[a]


        // return NotificationSchedule.values()[sharedpreferences.getValueInt("KEY")]
    }

    override fun setNotificationSchedule(notificationSchedule: NotificationSchedule) {
        notificationSchedule.ordinal

        //sharedpreferences.put("KEY", ordinal)
    }

    override fun setNotificationEnabled(enabled: Boolean) {
        // TODO

    }
}