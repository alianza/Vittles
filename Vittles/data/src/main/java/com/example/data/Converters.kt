package com.example.data

import androidx.room.TypeConverter
import java.util.*

/**
 * The converters in this class are used to convert non-persistable types in data models
 * to persistable types.
 *
 * @author https://developer.android.com/reference/android/arch/persistence/room/TypeConverter
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}