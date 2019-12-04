package com.example.vittles.enums

/**
 *
 * @author Jan-Willem van Bremen
 * @author Marc van Spronsen
 *
 * Enum class for settings and their key in the sharedPreferences DB
 *
 * @property value Value of the key
 */

enum class SettingKeys(var value: String) {
    Performance("SCANNING_PERFORMANCE"),
    NotificationTime("NOTIFICATION_TIME"),
    Notifications("NOTIFICATIONS"),
    Vibration("VIBRATION"),
}