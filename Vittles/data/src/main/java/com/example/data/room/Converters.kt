package com.example.data.room

import androidx.room.TypeConverter
import org.joda.time.DateTime

/**
 * The converters in this class are used to convert non-persistable types in data models
 * to persistable types.
 *
 * @author https://developer.android.com/reference/android/arch/persistence/room/TypeConverter
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): DateTime? {
        return DateTime(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: DateTime?): Long? {
        return date!!.millis
    }
}