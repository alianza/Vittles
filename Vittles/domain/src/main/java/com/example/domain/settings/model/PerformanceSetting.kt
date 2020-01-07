package com.example.domain.settings.model

/**
 * Enum class for the performance settings
 *
 * @property ms Milliseconds of the interval of the setting
 */

enum class PerformanceSetting(var ms: Long) {
    LOW(1000L),
    MEDIUM(500L),
    HIGH(200L)
}