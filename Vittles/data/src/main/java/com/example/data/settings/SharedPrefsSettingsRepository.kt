package com.example.data.settings

import android.content.Context
import com.example.domain.settings.SettingsRepository
import com.example.domain.settings.model.NotificationSchedule
import com.example.domain.settings.model.PerformanceSetting
import javax.inject.Inject

class  SharedPrefsSettingsRepository @Inject constructor(context: Context): SettingsRepository {

    /** The shared preferences instance. */
    private val sharedPreferences = SharedPreferenceHelper(context)

    private val performanceKey = "SCANNING_PERFORMANCE"
    private val notificationTimeKey = "NOTIFICATION_TIME"
    private val notificationsKey = "NOTIFICATIONS"
    private val vibrationKey = "VIBRATION"

    /** {@inheritDoc} */
    override fun getNotificationSchedule(): NotificationSchedule {
         return NotificationSchedule.values()[sharedPreferences.getValueInt(notificationTimeKey)]
    }

    /** {@inheritDoc} */
    override fun setNotificationSchedule(notificationSchedule: NotificationSchedule) {
        sharedPreferences.save(notificationTimeKey, notificationSchedule.ordinal)
    }

    /** {@inheritDoc} */
    override fun setNotificationEnabled(isEnabled: Boolean) {
        sharedPreferences.save(notificationsKey, isEnabled)
    }

    /** {@inheritDoc} */
    override fun getNotificationEnabled(): Boolean {
        return sharedPreferences.getValueBoolean(notificationsKey, true)
    }

    /** {@inheritDoc} */
    override fun getVibrationEnabled(): Boolean {
        return sharedPreferences.getValueBoolean(vibrationKey, true)
    }

    /** {@inheritDoc} */
    override fun setVibrationEnabled(isEnabled: Boolean) {
        sharedPreferences.save(vibrationKey, isEnabled)
    }

    /** {@inheritDoc} */
    override fun getPerformanceSetting(): PerformanceSetting {
       return PerformanceSetting.values()[sharedPreferences.getValueInt(performanceKey)]
    }

    /** {@inheritDoc} */
    override fun setPerformanceSetting(performanceSetting: PerformanceSetting) {
        sharedPreferences.save(performanceKey, performanceSetting.ordinal)
    }
}