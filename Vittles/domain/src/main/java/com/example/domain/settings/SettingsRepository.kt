package com.example.domain.settings

import com.example.domain.settings.model.NotificationSchedule

/**
 * Repository interface for the settings
 *
 * @author Fethi Tewelde
 */
interface SettingsRepository {
    /**
     * Gets the value of the notification schedule
     *
     * @return The enum of notification schedule options.
     */
    fun getNotificationSchedule(): NotificationSchedule

    /**
     * Sets the value of the notification schedule
     *
     * @param notificationSchedule The enum value to set
     */
    fun setNotificationSchedule(notificationSchedule: NotificationSchedule)

    /**
     * Gets the value of the notification
     *
     * @return boolean value of notification
     */
    fun getNotificationEnabled(): Boolean

    /**
     * Sets the value of the notification
     *
     * @param isEnabled boolean value to be set
     */
    fun setNotificationEnabled(isEnabled: Boolean)

    /**
     * Gets the value of the vibration
     *
     * @return boolean value of vibration
     */
    fun getVibrationEnabled(): Boolean

    /**
     * Sets the value of the notification schedule
     *
     * @param isEnabled the boolean value to be set
     */
    fun setVibrationEnabled(isEnabled: Boolean)
}
