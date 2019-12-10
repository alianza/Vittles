package com.example.domain.settings.model

/**
 * Enum class for the performance settings
 *
 * @property ms Milliseconds of the interval of the setting
 */

enum class PerformanceSetting(var ms: Int) {
    LOW(200),
    MEDIUM(500),
    HIGH(1000)
}