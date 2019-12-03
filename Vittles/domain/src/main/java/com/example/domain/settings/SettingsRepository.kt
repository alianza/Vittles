package com.example.domain.settings

import com.example.domain.settings.model.NotificationSchedule

/**
 * TODO
 *
 */
interface SettingsRepository {
    /**
     * TODO
     *
     * @return
     */
    fun getNotificationSchedule(): NotificationSchedule

    /**
     * TODO
     *
     * @param notificationSchedule
     */
    fun setNotificationSchedule(notificationSchedule: NotificationSchedule)

    /**
     * TODO
     *
     * @return
     */
    fun getNotificationEnabled(): Boolean

    /**
     * TODO
     *
     * @param isEnabled
     */
    fun setNotificationEnabled(isEnabled: Boolean)

    /**
     * TODO
     *
     * @return
     */
    fun getVibrationEnabled(): Boolean

    /**
     * TODO
     *
     * @param isEnabled
     */
    fun setVibrationEnabled(isEnabled: Boolean)
}
