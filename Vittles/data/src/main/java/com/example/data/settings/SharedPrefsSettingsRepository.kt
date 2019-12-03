package com.example.data.settings

import android.content.Context
import com.example.domain.settings.SettingsRepository
import com.example.domain.settings.model.NotificationSchedule
import javax.inject.Inject

class SharedPrefsSettingsRepository @Inject constructor(context: Context): SettingsRepository {

    private val sharedPreferences = SharedPreferenceHelper(context)

    override fun getNotificationSchedule(): NotificationSchedule {
         return NotificationSchedule.values()[sharedPreferences.getValueInt("NOTIFICATION_TIME")]
    }

    override fun setNotificationSchedule(notificationSchedule: NotificationSchedule) {
        sharedPreferences.save("NOTIFICATION_TIME", notificationSchedule.ordinal)
    }

    override fun setNotificationEnabled(isEnabled: Boolean) {
        sharedPreferences.save("NOTIFICATION", isEnabled)
    }

    override fun getNotificationEnabled(): Boolean {
        return sharedPreferences.getValueBoolean("NOTIFICATION", true)
    }

    override fun getVibrationEnabled(): Boolean {
        return sharedPreferences.getValueBoolean("VIBRATION", true)
    }

    override fun setVibrationEnabled(isEnabled: Boolean) {
        sharedPreferences.save("VIBRATION", isEnabled)
    }
}