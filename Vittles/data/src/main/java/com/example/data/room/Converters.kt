package com.example.data.room

import androidx.room.TypeConverter
import org.joda.time.DateTime

/**
 * The converters in this class are used to convert non-persistable types in data models
 * to persistable types.
 *
 * @author Jan-Willem van Bremen
 * @author Jeroen Flietstra
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