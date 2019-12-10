package com.example.data.settings

import android.content.Context
import com.example.domain.settings.SettingsRepository
import com.example.domain.settings.model.NotificationSchedule
import javax.inject.Inject

class SharedPrefsSettingsRepository @Inject constructor(context: Context): SettingsRepository {

    /** The shared preferences instance. */
    private val sharedPreferences = SharedPreferenceHelper(context)

    /** {@inheritDoc} */
    override fun getNotificationSchedule(): NotificationSchedule {
         return NotificationSchedule.values()[sharedPreferences.getValueInt("NOTIFICATION_TIME")]
    }

    /** {@inheritDoc} */
    override fun setNotificationSchedule(notificationSchedule: NotificationSchedule) {
        sharedPreferences.save("NOTIFICATION_TIME", notificationSchedule.ordinal)
    }

    /** {@inheritDoc} */
    override fun setNotificationEnabled(isEnabled: Boolean) {
        sharedPreferences.save("NOTIFICATION", isEnabled)
    }

    /** {@inheritDoc} */
    override fun getNotificationEnabled(): Boolean {
        return sharedPreferences.getValueBoolean("NOTIFICATION", true)
    }

    /** {@inheritDoc} */
    override fun getVibrationEnabled(): Boolean {
        return sharedPreferences.getValueBoolean("VIBRATION", true)
    }

    /** {@inheritDoc} */
    override fun setVibrationEnabled(isEnabled: Boolean) {
        sharedPreferences.save("VIBRATION", isEnabled)
    }
}